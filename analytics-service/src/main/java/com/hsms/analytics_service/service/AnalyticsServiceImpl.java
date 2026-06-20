package com.hsms.analytics_service.service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hsms.analytics_service.entity.AnalyticsReport;
import com.hsms.analytics_service.feignclient.PaymentClient;
import com.hsms.analytics_service.feignclient.TechnicianClient;
import com.hsms.analytics_service.feignclient.bookingServiceClient;
import com.hsms.analytics_service.model.CategoryDistributionDTO;
import com.hsms.analytics_service.model.DashboardResponseDTO;
import com.hsms.analytics_service.model.PaymentResponseDTO;
import com.hsms.analytics_service.model.ServiceRequestDetailResponseDTO;
import com.hsms.analytics_service.model.TechnicianDetailResponseDTO;
import com.hsms.analytics_service.repository.AnalyticsReportRepository;

@Service
public class AnalyticsServiceImpl implements AnalyticsService {

	@Autowired
	private AnalyticsReportRepository repo;

	@Autowired
	private bookingServiceClient bookingClient;

	@Autowired
	private TechnicianClient technicianClient;

	@Autowired
	private PaymentClient paymentClient;

	@Override
	public DashboardResponseDTO getDashboard() {

		List<ServiceRequestDetailResponseDTO> requests = bookingClient.getAllRequests();
		List<TechnicianDetailResponseDTO> technicians = technicianClient.getAllTechnicians();
		List<PaymentResponseDTO> payments = paymentClient.getAllPayments();
//		int totalBookings = 25;
//		double revenue = 12000.50;
//		List<TechnicianDetailResponseDTO> topTechnicians = List.of(
//			    new TechnicianDetailResponseDTO(101L, "John", null, 4.8),
//			    new TechnicianDetailResponseDTO(102L, "Alice", null, 4.7),
//			    new TechnicianDetailResponseDTO(103L, "Mike", null, 4.6)
//			);
//
//			List<CategoryDistributionDTO> categoryDistribution = List.of(
//			    new CategoryDistributionDTO(1L, 10),
//			    new CategoryDistributionDTO(2L, 8),
//			    new CategoryDistributionDTO(3L, 7)
//			);

		int totalBookings = requests.size();
		double revenue = payments.stream().mapToDouble(PaymentResponseDTO::getAmount).sum();

		List<TechnicianDetailResponseDTO> topTechnicians = technicians.stream()
				.sorted(Comparator.comparing(TechnicianDetailResponseDTO::getRating).reversed()).limit(5)
				.collect(Collectors.toList());
//
		List<CategoryDistributionDTO> categoryDistribution = requests.stream()
				.collect(Collectors.groupingBy(ServiceRequestDetailResponseDTO::getCategoryId, Collectors.counting()))
				.entrySet().stream()
				.map(entry -> new CategoryDistributionDTO(entry.getKey(), entry.getValue().intValue()))
				.collect(Collectors.toList());

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

