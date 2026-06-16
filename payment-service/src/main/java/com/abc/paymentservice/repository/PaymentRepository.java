package com.abc.paymentservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.abc.paymentservice.entity.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

}