package com.hsms.analytics_service.entity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MapKeyColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "analytics_reports")
public class AnalyticsReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reportId;   // <-- Primary Key for AnalyticsReport

    private int totalBookings;
    private double revenue;
    
    @ElementCollection
//    @CollectionTable(name = "analytics_top_technicians", joinColumns = @JoinColumn(name = "report_id"))
//    @Column(name = "technician_id")
    private List<Long> topTechnicians;   // IDs from Technician Service

    @ElementCollection
//    @CollectionTable(name = "analytics_category_distribution", joinColumns = @JoinColumn(name = "report_id"))
//    @MapKeyColumn(name = "category_id") // ID from ServiceCategory Service
//    @Column(name = "count")
    private Map<Long, Integer> categoryDistribution;
    private LocalDateTime generatedAt;
}


