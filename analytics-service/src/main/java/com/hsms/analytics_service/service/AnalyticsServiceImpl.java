package com.hsms.analytics_service.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hsms.analytics_service.entity.AnalyticsReport;
import com.hsms.analytics_service.feignclient.CategoryClient;
import com.hsms.analytics_service.feignclient.PaymentClient;
import com.hsms.analytics_service.feignclient.ServiceRequestClient;
import com.hsms.analytics_service.feignclient.TechnicianClient;
import com.hsms.analytics_service.model.DashboardResponseDTO;
import com.hsms.analytics_service.model.TechnicianDetailResponseDTO;
import com.hsms.analytics_service.repository.AnalyticsReportRepository;


@Service
public class AnalyticsServiceImpl implements AnalyticsService {
	@Autowired
	private AnalyticsReportRepository repo;
	@Autowired
	private ServiceRequestClient requestClient;
	@Autowired
	private TechnicianClient technicianClient;
	@Autowired
	private CategoryClient categoryClient;
	@Autowired
	private PaymentClient paymentClient;
	@Autowired
	private ModelMapper mapper;

	@Override
	public DashboardResponseDTO getDashboard() {
//		int totalBookings = requestClient.getAllRequests().size();
//		double revenue = paymentClient.getTotalRevenue();
//		List<TechnicianDetailResponseDTO> topTechnicians = technicianClient.getTopTechnicians();
//		Map<Long, Integer> categoryDistribution = categoryClient.getCategoryDistribution();
		int totalBookings = 25;
		double revenue = 12000.50;
		List<Long> topTechnicians = List.of(101L, 102L, 103L);
		Map<Long, Integer> categoryDistribution = Map.of(1L, 10, 2L, 8, 3L, 7);
		AnalyticsReport report = new AnalyticsReport();
		report.setTotalBookings(totalBookings);
		report.setRevenue(revenue);
//		report.setTopTechnicians(topTechnicians.stream().map(TechnicianDetailResponseDTO::getUserId).collect(Collectors.toList()));
		report.setCategoryDistribution(categoryDistribution);
		report.setGeneratedAt(LocalDateTime.now());

		return mapper.map(repo.save(report), DashboardResponseDTO.class);
	}
}
