package com.hsms.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import com.hsms.entity.Assignment;
import com.hsms.entity.AssignmentStatus;
import com.hsms.exception.AssignmentNotFoundException;
import com.hsms.exception.DuplicateAssignmentException;
import com.hsms.exception.TechnicianNotAvailableException;
import com.hsms.exception.UnauthorizedActionException;
import com.hsms.feignclient.NotificationClient;
import com.hsms.feignclient.ServiceRequestClient;
import com.hsms.feignclient.TechnicianClient;
import com.hsms.feignclient.UserServiceClient;
import com.hsms.model.AssignmentDetailResponseDTO;
import com.hsms.model.AssignmentRequestDTO;
import com.hsms.model.AssignmentResponseDTO;
import com.hsms.model.ServiceRequestDTO;
import com.hsms.model.TechnicianDetailResponseDTO;
import com.hsms.repository.AssignmentRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AssignmentServiceImpl implements AssignmentService {

	private final AssignmentRepository assignmentRepository;
	private final ModelMapper modelMapper;
	private final UserServiceClient userServiceClient;

	@Override
	public AssignmentResponseDTO assignTechnician(AssignmentRequestDTO requestDTO) {

		if (!currentUserHasRole("SERVICE_MANAGER")) {
			throw new UnauthorizedActionException("Only SERVICE_MANAGER can assign technicians");
		}
		assignmentRepository.findByServiceRequestId(requestDTO.getServiceRequestId()).ifPresent(existing -> {
			throw new DuplicateAssignmentException(
					"An assignment already exists for service request id: " + requestDTO.getServiceRequestId());
		});

		ResponseEntity<TechnicianDetailResponseDTO> response = userServiceClient
				.getTechnicianById(requestDTO.getTechnicianId());
		TechnicianDetailResponseDTO technician = response.getBody();

		if (technician == null || !"Available".equalsIgnoreCase(technician.getAvailability())) {
			throw new TechnicianNotAvailableException(
					"Technician with id " + requestDTO.getTechnicianId() + " is not available");
		}

		List<Assignment> existingAssignments = assignmentRepository.findByTechnicianIdAndStatusIn(
				requestDTO.getTechnicianId(), List.of(AssignmentStatus.ASSIGNED, AssignmentStatus.ACCEPTED));

		boolean conflict = existingAssignments.stream()
				.anyMatch(a -> a.getStartTime() != null && a.getStartTime().equals(requestDTO.getStartTime()));

		if (conflict) {
			throw new TechnicianNotAvailableException("Technician already has a job at this time");
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
				.orElseThrow(() -> new AssignmentNotFoundException("Assignment not found: " + assignmentId));
		AssignmentDetailResponseDTO dto = modelMapper.map(assignment, AssignmentDetailResponseDTO.class);
		ResponseEntity<TechnicianDetailResponseDTO> response = userServiceClient
				.getTechnicianById(assignment.getTechnicianId());
		dto.setTechnician(response.getBody());
		return dto;
	}

	@Override
	public List<AssignmentDetailResponseDTO> getAllAssignments() {
		return assignmentRepository.findAll().stream().map(a -> {
			AssignmentDetailResponseDTO dto = modelMapper.map(a, AssignmentDetailResponseDTO.class);
			ResponseEntity<TechnicianDetailResponseDTO> response = userServiceClient
					.getTechnicianById(a.getTechnicianId());
			dto.setTechnician(response.getBody());
			return dto;
		}).toList();
	}

	@Override
	public List<AssignmentResponseDTO> getAssignmentsByTechnician(Long technicianId) {
		return assignmentRepository.findByTechnicianId(technicianId).stream()
				.map(a -> modelMapper.map(a, AssignmentResponseDTO.class)).toList();
	}

	@Override
	public AssignmentResponseDTO updateAssignmentStatus(Long assignmentId, AssignmentStatus status) {
		Assignment assignment = assignmentRepository.findById(assignmentId)
				.orElseThrow(() -> new AssignmentNotFoundException("Assignment not found: " + assignmentId));
		if (assignment.getStatus() != AssignmentStatus.ASSIGNED
				&& assignment.getStatus() != AssignmentStatus.REASSIGNED) {
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
				.orElseThrow(() -> new AssignmentNotFoundException("Assignment not found: " + assignmentId));

		assignment.setTechnicianId(technicianId);
		assignment.setStatus(AssignmentStatus.REASSIGNED);
		assignment.setAssignedDate(LocalDateTime.now());

		Assignment updated = assignmentRepository.save(assignment);
		return modelMapper.map(updated, AssignmentResponseDTO.class);
	}

	@Override
	public void deleteAssignment(Long assignmentId) {
		Assignment assignment = assignmentRepository.findById(assignmentId)
				.orElseThrow(() -> new AssignmentNotFoundException("Assignment not found: " + assignmentId));
		assignmentRepository.delete(assignment);
	}

	@Override
	public AssignmentResponseDTO getByServiceRequestId(Long serviceRequestId) {
		Assignment assignment = assignmentRepository.findByServiceRequestId(serviceRequestId).orElseThrow(
				() -> new AssignmentNotFoundException("Assignment not found for service request: " + serviceRequestId));
		return modelMapper.map(assignment, AssignmentResponseDTO.class);
	}

	@Override
	public AssignmentResponseDTO acceptJob(Long assignmentId) {
		Assignment assignment = assignmentRepository.findById(assignmentId)
				.orElseThrow(() -> new AssignmentNotFoundException("Assignment not found: " + assignmentId));
		assignment.setStatus(AssignmentStatus.ACCEPTED);
		Assignment updated = assignmentRepository.save(assignment);
		return modelMapper.map(updated, AssignmentResponseDTO.class);
	}

	@Override
	public AssignmentResponseDTO rejectJob(Long assignmentId) {
		Assignment assignment = assignmentRepository.findById(assignmentId)
				.orElseThrow(() -> new AssignmentNotFoundException("Assignment not found: " + assignmentId));
		assignment.setStatus(AssignmentStatus.REJECTED);
		assignmentRepository.save(assignment);
		assignment.setStatus(AssignmentStatus.REASSIGNED);
		Assignment updated = assignmentRepository.save(assignment);
		return modelMapper.map(updated, AssignmentResponseDTO.class);
	}

	private boolean currentUserHasRole(String role) {
		ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		if (attrs == null)
			return false;
		HttpServletRequest request = attrs.getRequest();
		String headerRole = request.getHeader("X-Role");
		return role.equalsIgnoreCase(headerRole);
	}
}
