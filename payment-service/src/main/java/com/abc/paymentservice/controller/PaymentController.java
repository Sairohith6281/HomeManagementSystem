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
	public ResponseEntity<?> makePayment(@RequestBody PaymentRequestDTO paymentRequestDTO) {

		try {
			PaymentResponseDTO response = paymentService.makePayment(paymentRequestDTO);

			return new ResponseEntity<>(response, HttpStatus.CREATED);

		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/{paymentId}")
	public ResponseEntity<?> getPaymentById(@PathVariable Long paymentId) {

		try {
			PaymentDetailResponseDTO response = paymentService.findPaymentById(paymentId);

			return ResponseEntity.ok(response);

		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
	}
}