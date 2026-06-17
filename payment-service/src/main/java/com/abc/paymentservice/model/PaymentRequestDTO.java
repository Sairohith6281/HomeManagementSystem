package com.abc.paymentservice.model;

import com.abc.paymentservice.enums.PaymentMethod;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentRequestDTO {

    private Long serviceRequestId;
    private Double amount;
    private PaymentMethod method;
}