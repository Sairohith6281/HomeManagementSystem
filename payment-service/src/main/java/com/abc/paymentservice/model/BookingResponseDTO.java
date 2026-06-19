package com.abc.paymentservice.model;

import java.time.LocalDateTime;

import com.abc.paymentservice.enums.PaymentStatus;

import lombok.Data;

@Data

public class BookingResponseDTO {
	

	private Long requestId;
	

	private Long customerId;
	

	private Long categoryId;
	

	private String serviceType;
	
	
	private String address;
	

	private PaymentStatus status;
	

	private LocalDateTime createdAt;
	
}