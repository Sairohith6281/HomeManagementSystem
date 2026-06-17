package com.abc.paymentservice.model;

import com.abc.paymentservice.enums.PaymentMethod;
import com.abc.paymentservice.enums.PaymentStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentDetailResponseDTO {
	private Long paymentId;
	private Long serviceRequestId;
	private Double amount;
	private PaymentMethod method;
	private PaymentStatus status;
}
