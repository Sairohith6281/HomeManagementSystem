package com.hsms.execution_service.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.hsms.execution_service.model.PaymentDetailResponseDTO;
import com.hsms.execution_service.model.PaymentRequestDTO;
import com.hsms.execution_service.model.PaymentResponseDTO;

@FeignClient(name = "payment-service")
public interface PaymentClient {
    @PostMapping("/api/payments")
    PaymentResponseDTO createPayment(@RequestBody PaymentRequestDTO dto);

    @GetMapping("/api/payments/{id}")
    PaymentDetailResponseDTO getPayment(@PathVariable Long id);
}
