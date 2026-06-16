package com.hsms.execution_service.model;

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
    private String method;
    private String status;
}