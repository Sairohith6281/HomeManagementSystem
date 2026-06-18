package com.hsms.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import com.hsms.entity.Assignment;
import com.hsms.entity.AssignmentStatus;
import com.hsms.exception.TechnicianNotFoundException;
import com.hsms.exception.UnauthorizedActionException;
import com.hsms.feignclient.NotificationClient;
import com.hsms.feignclient.ServiceRequestClient;
import com.hsms.feignclient.TechnicianClient;
import com.hsms.model.AssignmentDetailResponseDTO;
import com.hsms.model.AssignmentRequestDTO;
import com.hsms.model.AssignmentResponseDTO;
import com.hsms.model.ServiceRequestDTO;
import com.hsms.model.TechnicianDTO;
import com.hsms.repository.AssignmentRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AssignmentServiceImpl implements AssignmentService {

    private final AssignmentRepository assignmentRepository;
    private final ModelMapper modelMapper;

    @Override
    public AssignmentResponseDTO assignTechnician(AssignmentRequestDTO requestDTO) {

        if (!currentUserHasRole("SERVICE_MANAGER")) {
            throw new UnauthorizedActionException("Only SERVICE_MANAGER can assign technicians");
        }

        if (assignmentRepository.existsByServiceRequestId(requestDTO.getServiceRequestId())) {
            throw new RuntimeException("Service request already assigned");
        }

       
        boolean technicianAvailable = true;
        boolean skillMatches = true;

        if (!technicianAvailable) {
            throw new TechnicianNotFoundException("Technician is not available");
        }
        if (!skillMatches) {
            throw new TechnicianNotFoundException("Technician skill does not match service category");
        }

        boolean hasActiveJob = assignmentRepository.existsByTechnicianIdAndStatusIn(
                requestDTO.getTechnicianId(),
                List.of(AssignmentStatus.ASSIGNED, AssignmentStatus.ACCEPTED)
        );

        if (hasActiveJob) {
            throw new TechnicianNotFoundException("Technician already has active assignment");
        }

        Assignment assignment = new Assignment();
        assignment.setTechnicianId(requestDTO.getTechnicianId());
        assignment.setServiceRequestId(requestDTO.getServiceRequestId());
        assignment.setAssignedDate(LocalDateTime.now());
        assignment.setStatus(AssignmentStatus.ASSIGNED);

        Assignment saved = assignmentRepository.save(assignment);
        return modelMapper.map(saved, AssignmentResponseDTO.class);
    }

    @Override
    public AssignmentDetailResponseDTO getAssignmentById(Long assignmentId) {
        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new RuntimeException("Assignment not found"));
        return modelMapper.map(assignment, AssignmentDetailResponseDTO.class);
    }

    @Override
    public List<AssignmentDetailResponseDTO> getAllAssignments() {
        return assignmentRepository.findAll().stream()
                .map(a -> modelMapper.map(a, AssignmentDetailResponseDTO.class))
                .toList();
    }

    @Override
    public List<AssignmentResponseDTO> getAssignmentsByTechnician(Long technicianId) {
        return assignmentRepository.findAll().stream()
                .filter(a -> a.getTechnicianId().equals(technicianId))
                .map(a -> modelMapper.map(a, AssignmentResponseDTO.class))
                .toList();
    }

    @Override
    public AssignmentResponseDTO updateAssignmentStatus(Long assignmentId, AssignmentStatus status) {
        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new RuntimeException("Assignment not found"));

        if (assignment.getStatus() != AssignmentStatus.ASSIGNED && assignment.getStatus() != AssignmentStatus.REASSIGNED) {
        	    throw new RuntimeException("Only ASSIGNED or REASSIGNED jobs can be accepted or rejected");
        	}

        if (status != AssignmentStatus.ACCEPTED && status != AssignmentStatus.REJECTED) {
            throw new RuntimeException("Only ACCEPTED or REJECTED status allowed");
        }

        assignment.setStatus(status);
        Assignment updated = assignmentRepository.save(assignment);
        return modelMapper.map(updated, AssignmentResponseDTO.class);
    }

    @Override
    public AssignmentResponseDTO reassignTechnician(Long assignmentId, Long technicianId) {
        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new RuntimeException("Assignment not found"));

        assignment.setTechnicianId(technicianId);
        assignment.setStatus(AssignmentStatus.REASSIGNED);
        Assignment updated = assignmentRepository.save(assignment);
        return modelMapper.map(updated, AssignmentResponseDTO.class);
    }

    @Override
    public void deleteAssignment(Long assignmentId) {
        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new RuntimeException("Assignment not found"));
        assignmentRepository.delete(assignment);
    }
    
    @Override
    public AssignmentResponseDTO getByServiceRequestId(Long serviceRequestId) {
        Assignment assignment = assignmentRepository.findByServiceRequestId(serviceRequestId)
                .orElseThrow(() ->new RuntimeException("Assignment not found"));
        return modelMapper.map(assignment, AssignmentResponseDTO.class);
    }

    private boolean currentUserHasRole(String role) {
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attrs == null) return false;
        HttpServletRequest request = attrs.getRequest();
        String headerRole = request.getHeader("X-Role");
        return role.equalsIgnoreCase(headerRole);
    }
}
