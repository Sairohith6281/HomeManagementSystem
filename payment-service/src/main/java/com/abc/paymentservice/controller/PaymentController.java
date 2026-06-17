package com.abc.paymentservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.abc.paymentservice.model.PaymentDetailResponseDTO;
import com.abc.paymentservice.model.PaymentRequestDTO;
import com.abc.paymentservice.model.PaymentResponseDTO;
import com.abc.paymentservice.service.PaymentService;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

	@Autowired
	private PaymentService paymentService;

	@PostMapping
	public ResponseEntity<PaymentResponseDTO> makePayment(@RequestBody PaymentRequestDTO paymentRequestDTO) {

		PaymentResponseDTO response = paymentService.makePayment(paymentRequestDTO);

		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@GetMapping("/{paymentId}")
	public ResponseEntity<PaymentDetailResponseDTO> getPaymentById(@PathVariable Long paymentId) {

		PaymentDetailResponseDTO response = paymentService.findPaymentById(paymentId);

		return ResponseEntity.ok(response);
	}
}