package com.hsms.analytics_service.entity;

import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "analytics_reports")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AnalyticsReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_id")
    private Long reportId;

    @Column(name = "total_bookings", nullable = false)
    private Integer totalBookings;

    @Column(name = "total_revenue", nullable = false)
    private Double revenue;

    @Column(name = "generated_at", nullable = false)
    private LocalDateTime generatedAt;
}