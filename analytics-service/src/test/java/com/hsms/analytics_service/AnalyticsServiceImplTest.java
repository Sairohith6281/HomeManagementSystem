package com.hsms.analytics_service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.hsms.analytics_service.entity.AnalyticsReport;
import com.hsms.analytics_service.feignclient.BookingServiceClient;
import com.hsms.analytics_service.feignclient.PaymentClient;
import com.hsms.analytics_service.feignclient.TechnicianClient;
import com.hsms.analytics_service.model.CategoryDistributionDTO;
import com.hsms.analytics_service.model.DashboardResponseDTO;
import com.hsms.analytics_service.model.PaymentResponseDTO;
import com.hsms.analytics_service.model.ServiceRequestDetailResponseDTO;
import com.hsms.analytics_service.model.TechnicianDetailResponseDTO;
import com.hsms.analytics_service.repository.AnalyticsReportRepository;
import com.hsms.analytics_service.service.AnalyticsServiceImpl;

@ExtendWith(MockitoExtension.class)
class AnalyticsServiceImplTest {

	@Mock
	private AnalyticsReportRepository repo;

	@Mock
	private BookingServiceClient bookingClient;

	@Mock
	private TechnicianClient technicianClient;

	@Mock
	private PaymentClient paymentClient;

	@InjectMocks
	private AnalyticsServiceImpl analyticsService;

	@Test
	void testGetDashboard() {

		// Service Requests
		ServiceRequestDetailResponseDTO request1 = new ServiceRequestDetailResponseDTO();
		request1.setCategoryId(1L);

		ServiceRequestDetailResponseDTO request2 = new ServiceRequestDetailResponseDTO();
		request2.setCategoryId(2L);

		when(bookingClient.getAllRequests()).thenReturn(List.of(request1, request2));

		// Technicians
		TechnicianDetailResponseDTO tech1 = new TechnicianDetailResponseDTO();
		tech1.setTechnicianId(1L);
		tech1.setRating(4.9);

		TechnicianDetailResponseDTO tech2 = new TechnicianDetailResponseDTO();
		tech2.setTechnicianId(2L);
		tech2.setRating(4.5);

		when(technicianClient.getAllTechnicians()).thenReturn(List.of(tech1, tech2));

		// Payments
		PaymentResponseDTO payment1 = new PaymentResponseDTO();
		payment1.setAmount(1000.0);

		PaymentResponseDTO payment2 = new PaymentResponseDTO();
		payment2.setAmount(2500.0);

		when(paymentClient.getAllPayments()).thenReturn(List.of(payment1, payment2));

		when(repo.save(any(AnalyticsReport.class))).thenAnswer(invocation -> invocation.getArgument(0));

		// Act
		DashboardResponseDTO response = analyticsService.getDashboard();

		// Assert
		assertNotNull(response);
		assertEquals(2, response.getTotalBookings());
		assertEquals(3500.0, response.getRevenue());
		assertNotNull(response.getGeneratedAt());

		assertEquals(2, response.getTopTechnicians().size());
		assertEquals(4.9, response.getTopTechnicians().get(0).getRating());

		List<CategoryDistributionDTO> categories = response.getCategoryDistribution();

		assertEquals(2, categories.size());

		verify(bookingClient).getAllRequests();
		verify(technicianClient).getAllTechnicians();
		verify(paymentClient).getAllPayments();
		verify(repo).save(any(AnalyticsReport.class));

		verifyNoMoreInteractions(bookingClient, technicianClient, paymentClient, repo);
	}
}