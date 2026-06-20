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
import com.hsms.model.NotificationDTO;
import com.hsms.model.ServiceRequestDTO;
import com.hsms.model.TechnicianDetailResponseDTO;
import com.hsms.model.UserDTO;
import com.hsms.repository.AssignmentRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AssignmentServiceImpl implements AssignmentService {

	private final AssignmentRepository assignmentRepository;
	private final ModelMapper modelMapper;
	private final TechnicianClient technicianClient;
	private final ServiceRequestClient serviceRequestClient;
	private final UserServiceClient userServiceClient;
	private final NotificationClient notificationClient;

	@Override
	public AssignmentResponseDTO assignTechnician(AssignmentRequestDTO requestDTO) {
		if (!currentUserHasRole("SERVICE_MANAGER")) {
			throw new UnauthorizedActionException("Only SERVICE_MANAGER can assign technicians");
		}

		assignmentRepository.findByServiceRequestId(requestDTO.getServiceRequestId()).ifPresent(existing -> {
			throw new DuplicateAssignmentException(
					"An assignment already exists for service request id: " + requestDTO.getServiceRequestId());
		});

		// ✅ Validate technician
		ResponseEntity<TechnicianDetailResponseDTO> techResponse = technicianClient
				.getTechnicianById(requestDTO.getTechnicianId());
		if (!techResponse.getStatusCode().is2xxSuccessful() || techResponse.getBody() == null) {
			throw new TechnicianNotAvailableException("Technician not found");
		}
		TechnicianDetailResponseDTO technician = techResponse.getBody();
		if (!"Available".equalsIgnoreCase(technician.getAvailability())) {
			throw new TechnicianNotAvailableException("Technician is not available");
		}

		// ✅ Validate service request
		ResponseEntity<ServiceRequestDTO> srResponse = serviceRequestClient
				.getServiceRequestById(requestDTO.getServiceRequestId());
		if (!srResponse.getStatusCode().is2xxSuccessful() || srResponse.getBody() == null) {
			throw new RuntimeException("Service request not found");
		}
		ServiceRequestDTO serviceRequest = srResponse.getBody();
		if (!"OPEN".equalsIgnoreCase(serviceRequest.getStatus())) {
			throw new RuntimeException("Service request is not valid for assignment");
		}

		// ✅ Validate user
		ResponseEntity<UserDTO> userResponse = userServiceClient.getUserById(requestDTO.getUserId());
		if (!userResponse.getStatusCode().is2xxSuccessful() || userResponse.getBody() == null) {
			throw new RuntimeException("User not found");
		}
		UserDTO user = userResponse.getBody();

		// ✅ Conflict check
		boolean conflict = assignmentRepository.existsByTechnicianIdAndStartTimeAndStatusIn(
				requestDTO.getTechnicianId(), requestDTO.getStartTime(),
				List.of(AssignmentStatus.ASSIGNED, AssignmentStatus.ACCEPTED));
		if (conflict) {
			throw new TechnicianNotAvailableException("Technician already has a job at this time");
		}

		// ✅ Save assignment
		Assignment assignment = new Assignment();
		assignment.setTechnicianId(requestDTO.getTechnicianId());
		assignment.setServiceRequestId(requestDTO.getServiceRequestId());
		assignment.setAssignedDate(LocalDateTime.now());
		assignment.setStartTime(requestDTO.getStartTime());
		assignment.setStatus(AssignmentStatus.ASSIGNED);

		Assignment saved = assignmentRepository.save(assignment);

		// ✅ Notify user
		notificationClient.sendNotification(new NotificationDTO(user.getId(),
				"New assignment created for service request " + serviceRequest.getRequestId(), LocalDateTime.now()));

		return modelMapper.map(saved, AssignmentResponseDTO.class);
	}
	
	@Override
	public List<AssignmentDetailResponseDTO> getAllAssignments() {
		return assignmentRepository.findAll().stream().map(a -> modelMapper.map(a, AssignmentDetailResponseDTO.class)).toList();
	}

	@Override
	public AssignmentResponseDTO acceptJob(Long assignmentId) {
		Assignment assignment = assignmentRepository.findById(assignmentId)
				.orElseThrow(() -> new AssignmentNotFoundException("Assignment not found: " + assignmentId));

		assignment.setStatus(AssignmentStatus.ACCEPTED);
		Assignment updated = assignmentRepository.save(assignment);

		// ✅ Notify manager
		notificationClient.sendNotification(new NotificationDTO(null, // could be managerId if available
				"Technician accepted assignment " + assignmentId, LocalDateTime.now()));

		return modelMapper.map(updated, AssignmentResponseDTO.class);
	}

	@Override
	public AssignmentResponseDTO rejectJob(Long assignmentId) {
		Assignment assignment = assignmentRepository.findById(assignmentId)
				.orElseThrow(() -> new AssignmentNotFoundException("Assignment not found: " + assignmentId));

		assignment.setStatus(AssignmentStatus.REJECTED);
		Assignment updated = assignmentRepository.save(assignment);

		// ✅ Notify manager
		notificationClient.sendNotification(new NotificationDTO(null, // could be managerId if available
				"Technician rejected assignment " + assignmentId, LocalDateTime.now()));

		return modelMapper.map(updated, AssignmentResponseDTO.class);
	}

	@Override
	public AssignmentResponseDTO reassignTechnician(Long assignmentId, Long technicianId, LocalDateTime startTime) {
		Assignment assignment = assignmentRepository.findById(assignmentId)
				.orElseThrow(() -> new AssignmentNotFoundException("Assignment not found: " + assignmentId));

		if (assignment.getStatus() != AssignmentStatus.REJECTED) {
			throw new RuntimeException("Only REJECTED jobs can be reassigned");
		}
		if (startTime == null) {
			throw new RuntimeException("Start time must be provided for reassignment");
		}

		// ✅ Validate technician
		ResponseEntity<TechnicianDetailResponseDTO> techResponse = technicianClient.getTechnicianById(technicianId);
		if (!techResponse.getStatusCode().is2xxSuccessful() || techResponse.getBody() == null) {
			throw new TechnicianNotAvailableException("Technician not found");
		}
		TechnicianDetailResponseDTO technician = techResponse.getBody();
		if (!"Available".equalsIgnoreCase(technician.getAvailability())) {
			throw new TechnicianNotAvailableException("Technician is not available");
		}

		// ✅ Conflict check
		boolean conflict = assignmentRepository
				.findByTechnicianIdAndStatusIn(technicianId,
						List.of(AssignmentStatus.ASSIGNED, AssignmentStatus.ACCEPTED))
				.stream().anyMatch(a -> !a.getId().equals(assignmentId) && a.getStartTime() != null
						&& a.getStartTime().toLocalDate().equals(startTime.toLocalDate()));

		if (conflict) {
			throw new TechnicianNotAvailableException("Technician already has a job at this time");
		}

		assignment.setTechnicianId(technicianId);
		assignment.setStartTime(startTime);
		assignment.setStatus(AssignmentStatus.REASSIGNED);
		assignment.setAssignedDate(LocalDateTime.now());

		Assignment updated = assignmentRepository.save(assignment);

		// ✅ Notify manager
		notificationClient.sendNotification(new NotificationDTO(null, // could be managerId if available
				"Assignment " + assignmentId + " reassigned to technician " + technicianId, LocalDateTime.now()));

		return modelMapper.map(updated, AssignmentResponseDTO.class);
	}

	@Override
	public void deleteAssignment(Long assignmentId) {
		Assignment assignment = assignmentRepository.findById(assignmentId)
				.orElseThrow(() -> new AssignmentNotFoundException("Assignment not found: " + assignmentId));
		assignmentRepository.delete(assignment);
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