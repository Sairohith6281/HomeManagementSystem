package com.hsms.userservice.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hsms.userservice.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

	Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
}
