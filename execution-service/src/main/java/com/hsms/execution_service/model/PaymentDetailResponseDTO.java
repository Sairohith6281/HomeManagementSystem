package com.hsms.execution_service.model;

import java.time.LocalDateTime;

import com.hsms.execution_service.entity.PaymentMethod;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDetailResponseDTO {
    private Long paymentId;
    private Long serviceRequestId;
    private Double amount;
    private PaymentMethod method;
    private String status;
    private LocalDateTime createdAt;
}