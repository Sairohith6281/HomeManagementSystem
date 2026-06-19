package com.hsms.booking.entity;

import com.hsms.booking.enums.ServiceRequestStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * ServiceRequest Entity - Represents customer service requests
 */
@Entity
@Table(name = "service_requests", indexes = {
    @Index(name = "idx_customer_id", columnList = "customer_id"),
    @Index(name = "idx_category_id", columnList = "category_id"),
    @Index(name = "idx_status", columnList = "status"),
    @Index(name = "idx_scheduled_date_time", columnList = "scheduled_date_time"),
    @Index(name = "idx_created_at", columnList = "created_at"),
    @Index(name = "idx_city", columnList = "city"),
    @Index(name = "idx_pincode", columnList = "pincode"),
    @Index(name = "idx_status_created_at", columnList = "status,created_at"),
    @Index(name = "idx_customer_status", columnList = "customer_id,status"),
    @Index(name = "idx_category_status", columnList = "category_id,status"),
    @Index(name = "idx_technician_status", columnList = "technician_id,status")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "request_id")
    private Long requestId;

    @Column(name = "customer_id", nullable = false)
    private Long customerId;

    @Column(name = "category_id", nullable = false)
    private Long categoryId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    @Builder.Default
    private ServiceRequestStatus status = ServiceRequestStatus.CREATED;

    @Column(name = "address", nullable = false, length = 255)
    private String address;

    @Column(name = "city", length = 100)
    private String city;

    @Column(name = "pincode", length = 10)
    private String pincode;

    @Column(name = "scheduled_date_time", nullable = false)
    private LocalDateTime scheduledDateTime;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "priority", length = 20)
    private String priority;

    @Column(name = "technician_id")
    private Long technicianId;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}

