package com.abc.paymentservice.service;

import java.time.LocalDateTime;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.abc.paymentservice.entity.Payment;
import com.abc.paymentservice.enums.PaymentStatus;
import com.abc.paymentservice.exception.InvalidPaymentException;
import com.abc.paymentservice.exception.PaymentNotFoundException;
import com.abc.paymentservice.exception.ServiceUnavailableException;
import com.abc.paymentservice.feignclient.BookingFeignClient;
import com.abc.paymentservice.model.BookingResponseDTO;
import com.abc.paymentservice.model.PaymentRequestDTO;
import com.abc.paymentservice.model.PaymentResponseDTO;
import com.abc.paymentservice.repository.PaymentRepository;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@Service
public class PaymentServiceImpl implements PaymentService {

	@Autowired
	private PaymentRepository paymentRepo;

	@Autowired
	private BookingFeignClient bookingFeignClient;

	@Autowired
	private ModelMapper modelMapper;

	// Create Payment
	@Override
	@CircuitBreaker(name = "paymentService", fallbackMethod = "createPaymentFallback")
	public PaymentResponseDTO createPayment(PaymentRequestDTO dto) {
      
		
		// Validate Booking
		BookingResponseDTO booking = bookingFeignClient.getBookingById(dto.getServiceRequestId());

		if (booking == null) {
			throw new InvalidPaymentException("Booking not found");
		}

		// Prevent Duplicate Payment
		paymentRepo.findByServiceRequestId(dto.getServiceRequestId()).ifPresent(p -> {
			throw new InvalidPaymentException("Payment already exists for booking " + dto.getServiceRequestId());
		});

		Payment payment = modelMapper.map(dto, Payment.class);

		payment.setPaymentStatus(PaymentStatus.SUCCESS);

		payment.setPaymentDate(LocalDateTime.now());

		Payment saved = paymentRepo.save(payment);

		return modelMapper.map(saved, PaymentResponseDTO.class);
	}

	// Fallback Method
	public PaymentResponseDTO createPaymentFallback(PaymentRequestDTO dto, Exception ex) {

		throw new ServiceUnavailableException("Booking Service is currently unavailable. Please try again later.");
	}

	// Get Payment By Id
	@Override
	public PaymentResponseDTO getPaymentById(Long paymentId) {

		Payment payment = paymentRepo.findById(paymentId)
				.orElseThrow(() -> new PaymentNotFoundException("Payment " + paymentId + " not found"));

		return modelMapper.map(payment, PaymentResponseDTO.class);
	}

	// Get Payments By Customer
	@Override
	public Page<PaymentResponseDTO> getPaymentsByCustomer(Long customerId, Pageable pageable) {

		return paymentRepo.findByCustomerId(customerId, pageable)
				.map(payment -> modelMapper.map(payment, PaymentResponseDTO.class));
	}

	// Update Payment Status
	@Override
	public PaymentResponseDTO updatePaymentStatus(Long paymentId, PaymentStatus status) {

		Payment payment = paymentRepo.findById(paymentId)
				.orElseThrow(() -> new PaymentNotFoundException("Payment " + paymentId + " not found"));

		payment.setPaymentStatus(status);

		Payment updated = paymentRepo.save(payment);

		return modelMapper.map(updated, PaymentResponseDTO.class);
	}

	// Show All Payments
	@Override
	public List<PaymentResponseDTO> showAllPayments() {

		return paymentRepo.findAll().stream().map(payment -> modelMapper.map(payment, PaymentResponseDTO.class))
				.toList();
	}

	@Override
	public void deletePayment(Long paymentId) {

		Payment payment = paymentRepo.findById(paymentId)
				.orElseThrow(() -> new PaymentNotFoundException("Payment not found"));

		paymentRepo.delete(payment);
	}

}