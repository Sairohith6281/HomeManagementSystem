package com.hsms.request.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.hsms.request.dto.Customer;
import com.hsms.request.dto.ServiceCategory;
import com.hsms.request.enums.ServiceRequestStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "service_requests")
public class ServiceRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "request_id")
    private Long requestId;

    private Customer customer;


    private ServiceCategory category;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private ServiceRequestStatus status = ServiceRequestStatus.CREATED;

    @Column(name = "address", nullable = false, length = 255)
    private String address;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

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

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    private Assignment assignment;

    private ServiceRecord serviceRecord;

    private Payment payment;

    private Feedback feedback;
}
