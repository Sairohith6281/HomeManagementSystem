package com.hsms.userservice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hsms.userservice.entity.Customer;
import com.hsms.userservice.entity.User;

public interface CustomerRepository extends JpaRepository<Customer, Long>{

	Optional<Customer> findByUserId(Long userId);
}
