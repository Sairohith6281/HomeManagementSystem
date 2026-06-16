package com.hsms.analytics_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.hsms.analytics_service.model.DashboardResponseDTO;
import com.hsms.analytics_service.service.AnalyticsService;

@RestController
@RequestMapping("/api/analytics")
public class AnalyticsController {
	
	@Autowired
    private AnalyticsService service;

    @GetMapping("/dashboard")
    public ResponseEntity<DashboardResponseDTO> dashboard() {
        return ResponseEntity.ok(service.getDashboard());
    }
}