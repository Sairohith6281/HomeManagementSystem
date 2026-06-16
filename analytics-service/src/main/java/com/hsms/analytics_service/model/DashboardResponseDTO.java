package com.hsms.analytics_service.model;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DashboardResponseDTO {
    private int totalBookings;
    private double revenue;
    private List<Long> topTechnicians;          
    private Map<Long, Integer> categoryDistribution; 
    private LocalDate generatedAt;
}
