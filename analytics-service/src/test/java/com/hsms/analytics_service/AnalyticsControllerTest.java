package com.hsms.analytics_service;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import com.hsms.analytics_service.controller.AnalyticsController;
import com.hsms.analytics_service.model.CategoryDistributionDTO;
import com.hsms.analytics_service.model.DashboardResponseDTO;
import com.hsms.analytics_service.model.TechnicianDetailResponseDTO;
import com.hsms.analytics_service.service.AnalyticsService;

class AnalyticsControllerTest {

	private final AnalyticsService analyticsService;
	private final AnalyticsController analyticsController;
	private MockMvc mockMvc;

	AnalyticsControllerTest() {
		this.analyticsService = Mockito.mock(AnalyticsService.class);
		this.analyticsController = new AnalyticsController(analyticsService);
	}

	@BeforeEach
	void setUp() {
		mockMvc = MockMvcBuilders.standaloneSetup(analyticsController).build();
	}

	@Test
	void testDashboard() throws Exception {

		TechnicianDetailResponseDTO technician = new TechnicianDetailResponseDTO();
		technician.setTechnicianId(1L);
		technician.setTechnicianName("John");
		technician.setRating(4.8);

		CategoryDistributionDTO category = new CategoryDistributionDTO(1L, 5);

		DashboardResponseDTO response = new DashboardResponseDTO();
		response.setTotalBookings(10);
		response.setRevenue(5000.0);
		response.setGeneratedAt(LocalDateTime.now());
		response.setTopTechnicians(List.of(technician));
		response.setCategoryDistribution(List.of(category));

		when(analyticsService.getDashboard()).thenReturn(response);

		mockMvc.perform(get("/api/analytics/dashboard")).andExpect(status().isOk())
				.andExpect(jsonPath("$.totalBookings").value(10)).andExpect(jsonPath("$.revenue").value(5000.0))
				.andExpect(jsonPath("$.topTechnicians[0].technicianId").value(1))
				.andExpect(jsonPath("$.categoryDistribution[0].categoryId").value(1))
				.andExpect(jsonPath("$.categoryDistribution[0].count").value(5));
	}
}