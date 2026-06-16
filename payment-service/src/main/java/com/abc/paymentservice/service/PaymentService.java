package com.abc.paymentservice.service;

import com.abc.paymentservice.model.PaymentDetailResponseDTO;
import com.abc.paymentservice.model.PaymentRequestDTO;
import com.abc.paymentservice.model.PaymentResponseDTO;

public interface PaymentService {

    PaymentResponseDTO makePayment(PaymentRequestDTO paymentRequestDTO);

    PaymentDetailResponseDTO findPaymentById(Long paymentId);
}