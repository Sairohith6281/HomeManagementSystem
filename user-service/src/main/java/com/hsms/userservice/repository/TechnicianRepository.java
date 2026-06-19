package com.hsms.userservice.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hsms.userservice.entity.Technician;
import com.hsms.userservice.entity.User;

public interface TechnicianRepository extends JpaRepository<Technician, Long>{

	Optional<Technician> findByUserId(Long userId);
}
