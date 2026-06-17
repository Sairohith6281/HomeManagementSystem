package com.abc.paymentservice.model;

import com.abc.paymentservice.enums.PaymentStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentResponseDTO {

    private Long paymentId;
    private Long serviceRequestId;
    private PaymentStatus status;
}