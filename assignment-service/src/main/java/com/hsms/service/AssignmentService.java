package com.hsms.service;

import java.time.LocalDateTime;
import java.util.List;
import com.hsms.entity.AssignmentStatus;
import com.hsms.model.AssignmentDetailResponseDTO;
import com.hsms.model.AssignmentRequestDTO;
import com.hsms.model.AssignmentResponseDTO;

public interface AssignmentService {
    AssignmentResponseDTO assignTechnician(AssignmentRequestDTO requestDTO);
    List<AssignmentDetailResponseDTO> getAllAssignments();
    AssignmentResponseDTO reassignTechnician(Long assignmentId, Long technicianId,LocalDateTime startTime);
	void deleteAssignment(Long assignmentId);
	AssignmentResponseDTO acceptJob(Long assignmentId);
    AssignmentResponseDTO rejectJob(Long assignmentId);
}