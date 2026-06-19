package com.abc.paymentservice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.abc.paymentservice.enums.PaymentStatus;
import com.abc.paymentservice.model.PaymentRequestDTO;
import com.abc.paymentservice.model.PaymentResponseDTO;
import com.abc.paymentservice.service.PaymentService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

@Autowired
private PaymentService paymentService;

// Create Payment
@PostMapping("/save")
public ResponseEntity<PaymentResponseDTO> createPayment(
        @Valid @RequestBody PaymentRequestDTO dto) {

    return ResponseEntity.status(HttpStatus.CREATED)
            .body(paymentService.createPayment(dto));
}

// Get Payment By Id
@GetMapping("/{paymentId}")
public ResponseEntity<PaymentResponseDTO> getPaymentById(
        @PathVariable Long paymentId) {

    return ResponseEntity.status(HttpStatus.OK)
            .body(paymentService.getPaymentById(paymentId));
}

// Get Payments By Customer
@GetMapping("/customer/{customerId}")
public ResponseEntity<Page<PaymentResponseDTO>>
        getPaymentsByCustomer(
                @PathVariable Long customerId,
                Pageable pageable) {

    return ResponseEntity.status(HttpStatus.OK)
            .body(paymentService.getPaymentsByCustomer(
                    customerId,
                    pageable));
}

// Update Payment Status
@PutMapping("/status/{paymentId}")
public ResponseEntity<PaymentResponseDTO>
        updatePaymentStatus(
                @PathVariable Long paymentId,
                @RequestParam PaymentStatus status) {

    return ResponseEntity.status(HttpStatus.OK)
            .body(paymentService.updatePaymentStatus(
                    paymentId,
                    status));
}

// Show All Payments
@GetMapping("/all")
public ResponseEntity<List<PaymentResponseDTO>>
        showAllPayments() {

    return ResponseEntity.status(HttpStatus.OK)
            .body(paymentService.showAllPayments());
}


@DeleteMapping("/{paymentId}")
public ResponseEntity<String> deletePayment(
        @PathVariable Long paymentId) {

    paymentService.deletePayment(paymentId);

    return ResponseEntity.ok(
            "Payment deleted successfully");
}

}