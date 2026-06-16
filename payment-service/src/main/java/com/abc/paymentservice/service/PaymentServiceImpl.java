package com.abc.paymentservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.abc.paymentservice.entity.Payment;
import com.abc.paymentservice.exception.ResourceNotFoundException;
import com.abc.paymentservice.model.PaymentDetailResponseDTO;
import com.abc.paymentservice.model.PaymentRequestDTO;
import com.abc.paymentservice.model.PaymentResponseDTO;
import com.abc.paymentservice.repository.PaymentRepository;

@Service
public class PaymentServiceImpl implements PaymentService {

	@Autowired
	private PaymentRepository paymentRepo;

	@Override
	public PaymentResponseDTO makePayment(PaymentRequestDTO paymentRequestDTO) {

		Payment payment = new Payment();
		payment.setServiceRequestId(paymentRequestDTO.getServiceRequestId());
		payment.setAmount(paymentRequestDTO.getAmount());
		payment.setMethod(paymentRequestDTO.getMethod());

		// Default payment status
		payment.setStatus("SUCCESS");

		Payment savedPayment = paymentRepo.save(payment);

		PaymentResponseDTO response = new PaymentResponseDTO();
		response.setPaymentId(savedPayment.getId());
		response.setServiceRequestId(savedPayment.getServiceRequestId());
		response.setStatus(savedPayment.getStatus());

		return response;
	}

	@Override
	public PaymentDetailResponseDTO findPaymentById(Long paymentId) {

		Payment payment = paymentRepo.findById(paymentId)
				.orElseThrow(() -> new ResourceNotFoundException("Payment not found with id: " + paymentId));

		PaymentDetailResponseDTO response = new PaymentDetailResponseDTO();
		response.setPaymentId(payment.getId());
		response.setServiceRequestId(payment.getServiceRequestId());
		response.setAmount(payment.getAmount());
		response.setMethod(payment.getMethod());
		response.setStatus(payment.getStatus());

		return response;
	}
}