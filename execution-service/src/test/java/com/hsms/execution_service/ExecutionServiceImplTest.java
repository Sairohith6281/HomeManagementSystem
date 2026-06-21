package com.hsms.execution_service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import com.hsms.execution_service.entity.PaymentMethod;
import com.hsms.execution_service.entity.ServiceRecord;
import com.hsms.execution_service.exception.ResourceNotFoundException;
import com.hsms.execution_service.feignclient.AssignmentClient;
import com.hsms.execution_service.feignclient.BookingserviceClient;
import com.hsms.execution_service.feignclient.PaymentClient;
import com.hsms.execution_service.model.AssignmentResponseDTO;
import com.hsms.execution_service.model.ServiceRecordDetailResponseDTO;
import com.hsms.execution_service.model.ServiceRecordRequestDTO;
import com.hsms.execution_service.model.ServiceRecordResponseDTO;
import com.hsms.execution_service.repository.ServiceRecordRepository;
import com.hsms.execution_service.service.ServiceRecordServiceImpl;

@ExtendWith(MockitoExtension.class)
class ServiceRecordServiceImplTest {

	@Mock
	private ServiceRecordRepository repo;

	@Mock
	private BookingserviceClient bookingClient;

	@Mock
	private PaymentClient paymentClient;

	@Mock
	private AssignmentClient assignmentClient;

	@Mock
	private ModelMapper mapper;

	@InjectMocks
	private ServiceRecordServiceImpl service;

	// ---------------- START ----------------
	@Test
	void start_shouldCreateServiceRecord_whenValidInput() {

		ServiceRecordRequestDTO dto = new ServiceRecordRequestDTO();
		dto.setServiceRequestId(1L);

		ServiceRecordRequestDTO booking = new ServiceRecordRequestDTO();
		booking.setStatus("ASSIGNED");

		when(bookingClient.getRequest(1L)).thenReturn(booking);

		AssignmentResponseDTO assignment = new AssignmentResponseDTO();
		assignment.setStatus("ACCEPTED");

		when(assignmentClient.getByServiceRequestId(1L)).thenReturn(assignment);

		ServiceRecord saved = new ServiceRecord();
		saved.setServiceId(10L);
		saved.setServiceRequestId(1L);
		saved.setStatus("IN_PROGRESS");

		when(repo.save(any(ServiceRecord.class))).thenReturn(saved);

		ServiceRecordResponseDTO response = new ServiceRecordResponseDTO();
		when(mapper.map(saved, ServiceRecordResponseDTO.class)).thenReturn(response);

		ServiceRecordResponseDTO result = service.start(dto);

		assertNotNull(result);

		verify(repo).save(any(ServiceRecord.class));
		verify(bookingClient).updateStatus(1L, "IN_PROGRESS");
	}

	// ---------------- COMPLETE ----------------
	@Test
	void complete_shouldFinishService_andTriggerPayment() {

		ServiceRecordRequestDTO dto = new ServiceRecordRequestDTO();
		dto.setRemarks("Done");
		dto.setActualCost(500.0);
		dto.setPaymentMethod(PaymentMethod.CARD);

		ServiceRecord record1 = new ServiceRecord();
		record1.setServiceId(1L);
		record1.setServiceRequestId(100L);
		record1.setStatus("IN_PROGRESS");

		when(repo.findById(1L)).thenReturn(Optional.of(record1));

		ServiceRecord saved = new ServiceRecord();
		saved.setServiceId(1L);
		saved.setServiceRequestId(100L);
		saved.setStatus("COMPLETED");

		when(repo.save(any(ServiceRecord.class))).thenReturn(saved);

		ServiceRecordDetailResponseDTO response = new ServiceRecordDetailResponseDTO();
		when(mapper.map(saved, ServiceRecordDetailResponseDTO.class)).thenReturn(response);

		ServiceRecordDetailResponseDTO result = service.complete(1L, dto);

		assertNotNull(result);

		verify(paymentClient).createPayment(any());
		verify(bookingClient).updateStatus(100L, "COMPLETED");
		verify(repo).save(any(ServiceRecord.class));
	}

	// ---------------- GET ----------------
	@Test
	void get_shouldReturnServiceRecord_whenFound() {

		ServiceRecord serviceRecord = new ServiceRecord();
		serviceRecord.setServiceId(1L);

		when(repo.findById(1L)).thenReturn(Optional.of(serviceRecord));

		ServiceRecordDetailResponseDTO dto = new ServiceRecordDetailResponseDTO();
		when(mapper.map(serviceRecord, ServiceRecordDetailResponseDTO.class)).thenReturn(dto);

		ServiceRecordDetailResponseDTO result = service.get(1L);

		assertNotNull(result);

		verify(repo).findById(1L);
	}

	// ---------------- NEGATIVE CASE ----------------
	@Test
	void complete_shouldThrowException_whenRecordNotFound() {

		when(repo.findById(1L)).thenReturn(Optional.empty());

		ServiceRecordRequestDTO dto = new ServiceRecordRequestDTO();
		dto.setPaymentMethod(PaymentMethod.CARD);

		assertThrows(ResourceNotFoundException.class, () -> service.complete(1L, dto));

		verify(paymentClient, never()).createPayment(any());
	}
}