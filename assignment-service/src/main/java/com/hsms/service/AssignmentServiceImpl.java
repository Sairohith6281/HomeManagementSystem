package com.hsms.service;

import java.time.LocalDateTime;
import java.util.List;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.hsms.entity.Assignment;
import com.hsms.entity.AssignmentStatus;
import com.hsms.exception.TechnicianNotFoundException;
import com.hsms.exception.UnauthorizedActionException;
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
//		if (!currentUserHasRole("SERVICE_MANAGER")) {
//			throw new UnauthorizedActionException("Only SERVICE_MANAGER can assign technicians");
//		}
//
//		// 🚫 Prevent overlapping jobs
//		boolean hasActiveJob = assignmentRepository.existsByTechnicianIdAndStatusIn(requestDTO.getTechnicianId(),
//				List.of(AssignmentStatus.ASSIGNED, AssignmentStatus.ACCEPTED));
//		if (hasActiveJob) {
//			throw new TechnicianNotFoundException("Technician already has an active job");
//		}
//
//		// ✅ Validate technician skill & availability (mock or Feign)
//		TechnicianDTO technician = fetchTechnician(requestDTO.getTechnicianId());
//		ServiceRequestDTO request = fetchServiceRequest(requestDTO.getServiceRequestId());
//
//		if (!technician.getAvailability()) {
//			throw new TechnicianNotFoundException("Technician is not available");
//		}
//		if (!technician.getSkill().equalsIgnoreCase(request.getServiceType())) {
//			throw new TechnicianNotFoundException("Technician skill does not match service category");
//		}
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
		return assignmentRepository.findAll().stream().map(a -> modelMapper.map(a, AssignmentDetailResponseDTO.class))
				.toList();
	}

	@Override
	public List<AssignmentResponseDTO> getAssignmentsByTechnician(Long technicianId) {
		return assignmentRepository.findAll().stream().filter(a -> a.getTechnicianId().equals(technicianId))
				.map(a -> modelMapper.map(a, AssignmentResponseDTO.class)).toList();
	}

	@Override
	public AssignmentResponseDTO updateAssignmentStatus(Long assignmentId, AssignmentStatus status) {
		Assignment assignment = assignmentRepository.findById(assignmentId)
				.orElseThrow(() -> new RuntimeException("Assignment not found"));
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

	// --- Helper methods (mocked for now, replace with Feign later) ---
	private boolean currentUserHasRole(String role) {
		ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
	    if (attrs == null) return false;
	    HttpServletRequest request = attrs.getRequest();
	    String headerRole = request.getHeader("X-Role");
	    return role.equalsIgnoreCase(headerRole);
	}

	private TechnicianDTO fetchTechnician(Long technicianId) {
		// For now, mock data — later replace with Feign Client
		return new TechnicianDTO(technicianId, "John Doe", "john@example.com", "AC Repair", 5, true, 4.5);
	}

	private ServiceRequestDTO fetchServiceRequest(Long requestId) {
		// For now, mock data — later replace with Feign Client
		return new ServiceRequestDTO(requestId, 1001L, 1L, "AC Repair", "PENDING", "123 Street", LocalDateTime.now());
	}

}
