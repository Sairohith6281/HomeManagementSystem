package com.hsms.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.hsms.entity.Assignment;
import com.hsms.entity.AssignmentStatus;

public interface AssignmentRepository extends JpaRepository<Assignment, Long> {

	Optional<Assignment> findByServiceRequestId(Long serviceRequestId);

	List<Assignment> findByTechnicianIdAndStatusIn(Long technicianId, List<AssignmentStatus> of);

	Optional<Assignment> findByTechnicianId(Long technicianId);

	boolean existsByTechnicianIdAndStartTimeAndStatusIn(Long technicianId, LocalDateTime startTime,
			List<AssignmentStatus> of);
}