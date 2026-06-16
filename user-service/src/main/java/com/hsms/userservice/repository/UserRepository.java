package com.hsms.userservice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hsms.userservice.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByEmail(String email); // find by Email

	boolean existsByEmail(String email); // Existing or not
}
