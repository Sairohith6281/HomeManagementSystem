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

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AnalyticsServiceImpl implements AnalyticsService {
	private final AnalyticsReportRepository repo;
	private final ServiceRequestClient requestClient;
	private final TechnicianClient technicianClient;
	private final CategoryClient categoryClient;
	private final PaymentClient paymentClient;
	private final ModelMapper mapper;

	@Override
	public DashboardResponseDTO getDashboard() {
		int totalBookings = requestClient.getAllRequests().size();
		double revenue = paymentClient.getTotalRevenue();
		List<TechnicianDetailResponseDTO> topTechnicians = technicianClient.getTopTechnicians();
		Map<Long, Integer> categoryDistribution = categoryClient.getCategoryDistribution();
		AnalyticsReport report = new AnalyticsReport();
		report.setTotalBookings(totalBookings);
		report.setRevenue(revenue);
		report.setTopTechnicians(topTechnicians.stream().map(TechnicianDetailResponseDTO::getUserId).collect(Collectors.toList()));
		report.setCategoryDistribution(categoryDistribution);
		report.setGeneratedAt(LocalDateTime.now());

		return mapper.map(repo.save(report), DashboardResponseDTO.class);
	}
}
