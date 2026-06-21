package com.hsms.assignmentservice;

import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import com.hsms.entity.Assignment;
import com.hsms.entity.AssignmentStatus;
import com.hsms.repository.AssignmentRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AssignmentRepositoryTest {

	@Autowired
	private AssignmentRepository assignmentRepository;

	@Test
	void testFindByServiceRequestId() {

		Assignment assignment = new Assignment();
		assignment.setServiceRequestId(100L);
		assignment.setTechnicianId(1L);
		assignment.setStatus(AssignmentStatus.ASSIGNED);
		assignment.setAssignedDate(LocalDateTime.now());
		assignment.setStartTime(LocalDateTime.now());

		assignmentRepository.save(assignment);

		Optional<Assignment> result = assignmentRepository.findByServiceRequestId(100L);

		assertTrue(result.isPresent());
		assertEquals(100L, result.get().getServiceRequestId());
	}

	@Test
	void testFindByTechnicianIdAndStatusIn() {

		Assignment assignment = new Assignment();
		assignment.setServiceRequestId(101L);
		assignment.setTechnicianId(10L);
		assignment.setStatus(AssignmentStatus.ASSIGNED);
		assignment.setAssignedDate(LocalDateTime.now());
		assignment.setStartTime(LocalDateTime.now());

		assignmentRepository.save(assignment);

		List<Assignment> result = assignmentRepository.findByTechnicianIdAndStatusIn(10L,
				List.of(AssignmentStatus.ASSIGNED));

		assertEquals(1, result.size());
	}

	@Test
	void testFindByTechnicianId() {

		Assignment assignment = new Assignment();
		assignment.setServiceRequestId(102L);
		assignment.setTechnicianId(20L);
		assignment.setStatus(AssignmentStatus.ACCEPTED);
		assignment.setAssignedDate(LocalDateTime.now());
		assignment.setStartTime(LocalDateTime.now());

		assignmentRepository.save(assignment);

		Optional<Assignment> result = assignmentRepository.findByTechnicianId(20L);

		assertTrue(result.isPresent());
		assertEquals(20L, result.get().getTechnicianId());
	}

	@Test
	void testExistsByTechnicianIdAndStartTimeAndStatusIn() {

		LocalDateTime startTime = LocalDateTime.of(2026, 6, 21, 10, 30);

		Assignment assignment = new Assignment();
		assignment.setServiceRequestId(103L);
		assignment.setTechnicianId(30L);
		assignment.setAssignedDate(LocalDateTime.now().withNano(0));
		assignment.setStartTime(startTime);
		assignment.setStatus(AssignmentStatus.ASSIGNED);

		assignmentRepository.saveAndFlush(assignment);

		boolean exists = assignmentRepository.existsByTechnicianIdAndStartTimeAndStatusIn(30L, startTime,
				List.of(AssignmentStatus.ASSIGNED));

		assertTrue(exists);
	}
}