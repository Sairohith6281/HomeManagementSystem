package com.hsms.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.hsms.entity.Assignment;
import com.hsms.entity.AssignmentStatus;

public interface AssignmentRepository extends JpaRepository<Assignment, Long> {

	boolean existsByTechnicianIdAndStatusIn(Long technicianId, List<AssignmentStatus> status);
	boolean existsByServiceRequestId(Long serviceRequestId);
	Optional<Assignment> findByServiceRequestId(Long serviceRequestId);
	 List<Assignment> findByTechnicianId(Long technicianId);
}