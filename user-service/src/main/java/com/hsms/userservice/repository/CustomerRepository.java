package com.hsms.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hsms.userservice.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long>{

}