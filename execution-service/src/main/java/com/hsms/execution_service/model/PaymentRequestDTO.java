package com.hsms.execution_service.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequestDTO {
    private Long serviceRequestId;
    private Double amount;
    private String method; // e.g., ONLINE, CASH
}