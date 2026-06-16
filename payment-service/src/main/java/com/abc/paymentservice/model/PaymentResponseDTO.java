package com.abc.paymentservice.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentResponseDTO {

    private Long paymentId;
    private Long serviceRequestId;
    private String status;
}