package com.hsms.request.dto;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.hsms.request.entity.ServiceRequest;
import com.hsms.request.enums.PaymentMethod;
import com.hsms.request.enums.PaymentStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor


public class Payment {

    private Long paymentId;

    private ServiceRequest serviceRequest;

    private Double amount;

    private PaymentMethod paymentMethod;  // Enum

    private PaymentStatus status = PaymentStatus.PENDING; // Enum


    private String transactionId;

    private LocalDateTime paymentDate;

    private Double refundAmount = 0.0;

    private LocalDateTime refundDate;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
