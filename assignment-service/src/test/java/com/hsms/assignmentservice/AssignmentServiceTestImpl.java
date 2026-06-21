package com.hsms.assignmentservice;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import com.hsms.entity.Assignment;
import com.hsms.entity.AssignmentStatus;
import com.hsms.feignclient.BookingServiceClient;
import com.hsms.feignclient.NotificationClient;
import com.hsms.feignclient.TechnicianClient;
import com.hsms.feignclient.UserServiceClient;
import com.hsms.model.AssignmentRequestDTO;
import com.hsms.model.AssignmentResponseDTO;
import com.hsms.model.NotificationDTO;
import com.hsms.model.ServiceRequestDTO;
import com.hsms.model.TechnicianDetailResponseDTO;
import com.hsms.model.UserDTO;
import com.hsms.repository.AssignmentRepository;
import com.hsms.service.AssignmentServiceImpl;

@ExtendWith(MockitoExtension.class)
class AssignmentServiceImplTest {

	@Mock
	private AssignmentRepository assignmentRepository;

	@Mock
	private ModelMapper modelMapper;

	@Mock
	private TechnicianClient technicianClient;

	@Mock
	private BookingServiceClient bookingServiceClient;

	@Mock
	private UserServiceClient userServiceClient;

	@Mock
	private NotificationClient notificationClient;

	@InjectMocks
	private AssignmentServiceImpl assignmentService;

	@BeforeEach
	void setup() {

		MockHttpServletRequest request = new MockHttpServletRequest();
		request.addHeader("X-User-Role", "SERVICE_MANAGER");

		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
	}

	private TechnicianDetailResponseDTO technician() {

		TechnicianDetailResponseDTO dto = new TechnicianDetailResponseDTO();

		dto.setUserId(10L);
		dto.setAvailability("Available");

		return dto;
	}

	private ServiceRequestDTO serviceRequest() {

		ServiceRequestDTO dto = new ServiceRequestDTO();

		dto.setRequestId(100L);
		dto.setStatus("CREATED");

		return dto;
	}

	private UserDTO user() {

		UserDTO dto = new UserDTO();

		dto.setId(1L);

		return dto;
	}

	@Test
	void testAssignTechnicianSuccess() {

		AssignmentRequestDTO request = new AssignmentRequestDTO();

		request.setUserId(1L);
		request.setTechnicianId(10L);
		request.setServiceRequestId(100L);
		request.setStartTime(LocalDateTime.now().withNano(0));

		when(assignmentRepository.findByServiceRequestId(anyLong())).thenReturn(Optional.empty());

		when(technicianClient.getTechnicianById(anyLong()))
				.thenReturn(new ResponseEntity<>(technician(), HttpStatus.OK));

		when(bookingServiceClient.getServiceRequestById(anyLong()))
				.thenReturn(new ResponseEntity<>(serviceRequest(), HttpStatus.OK));

		when(userServiceClient.getUserById(anyLong())).thenReturn(new ResponseEntity<>(user(), HttpStatus.OK));

		when(assignmentRepository.existsByTechnicianIdAndStartTimeAndStatusIn(anyLong(), any(LocalDateTime.class),
				anyList())).thenReturn(false);

		Assignment assignment = new Assignment();

		assignment.setId(1L);
		assignment.setTechnicianId(10L);
		assignment.setServiceRequestId(100L);
		assignment.setAssignedDate(LocalDateTime.now());
		assignment.setStatus(AssignmentStatus.ASSIGNED);
		assignment.setStartTime(request.getStartTime());

		when(assignmentRepository.save(any(Assignment.class))).thenReturn(assignment);

		AssignmentResponseDTO response = new AssignmentResponseDTO();

		when(modelMapper.map(any(Assignment.class), eq(AssignmentResponseDTO.class))).thenReturn(response);

		AssignmentResponseDTO result = assignmentService.assignTechnician(request);

		assertNotNull(result);

		verify(assignmentRepository).save(any(Assignment.class));

		verify(notificationClient).sendNotification(any(NotificationDTO.class));

		verify(bookingServiceClient).updateStatus(100L, "ASSIGNED");
	}
}