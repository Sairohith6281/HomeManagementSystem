package com.hsms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.hsms.entity.Assignment;

public interface AssignmentRepository extends JpaRepository<Assignment, Long> {
	
}