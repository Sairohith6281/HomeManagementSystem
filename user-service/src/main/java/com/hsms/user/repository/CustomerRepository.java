package com.hsms.user.repository;

import com.hsms.user.entity.Customer;
import com.hsms.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findByUserEmail(String email);
    boolean existsByUser(User user);
}
