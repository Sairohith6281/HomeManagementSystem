package com.hsms.analytics_service.service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
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

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AnalyticsServiceImpl implements AnalyticsService {

	private final AnalyticsReportRepository repo;
	private final BookingServiceClient bookingClient;
	private final TechnicianClient technicianClient;
	private final PaymentClient paymentClient;

	@Override
	public DashboardResponseDTO getDashboard() {

		List<ServiceRequestDetailResponseDTO> requests = bookingClient.getAllRequests();
		List<TechnicianDetailResponseDTO> technicians = technicianClient.getAllTechnicians();
		List<PaymentResponseDTO> payments = paymentClient.getAllPayments();

		int totalBookings = requests.size();
		double revenue = payments.stream().mapToDouble(PaymentResponseDTO::getAmount).sum();

		List<TechnicianDetailResponseDTO> topTechnicians = technicians.stream()
				.sorted(Comparator.comparing(TechnicianDetailResponseDTO::getRating).reversed()).limit(5).toList();

		List<CategoryDistributionDTO> categoryDistribution = requests.stream()
				.collect(Collectors.groupingBy(ServiceRequestDetailResponseDTO::getCategoryId, Collectors.counting()))
				.entrySet().stream()
				.map(entry -> new CategoryDistributionDTO(entry.getKey(), entry.getValue().intValue())).toList();

		AnalyticsReport report = new AnalyticsReport();
		report.setTotalBookings(totalBookings);
		report.setRevenue(revenue);
		report.setGeneratedAt(LocalDateTime.now());
		repo.save(report);

		DashboardResponseDTO response = new DashboardResponseDTO();
		response.setTotalBookings(totalBookings);
		response.setRevenue(revenue);
		response.setGeneratedAt(report.getGeneratedAt());
		response.setTopTechnicians(topTechnicians);
		response.setCategoryDistribution(categoryDistribution);
		return response;
	}
}
