package com.abc.paymentservice.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentRequestDTO {

    private Long serviceRequestId;
    private Double amount;
    private String method;
}