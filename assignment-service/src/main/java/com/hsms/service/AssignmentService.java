package com.hsms.service;

import java.util.List;
import com.hsms.entity.AssignmentStatus;
import com.hsms.model.AssignmentDetailResponseDTO;
import com.hsms.model.AssignmentRequestDTO;
import com.hsms.model.AssignmentResponseDTO;

public interface AssignmentService {

    AssignmentResponseDTO assignTechnician(AssignmentRequestDTO requestDTO);
    AssignmentDetailResponseDTO getAssignmentById(Long assignmentId);
    List<AssignmentDetailResponseDTO> getAllAssignments();
    List<AssignmentResponseDTO> getAssignmentsByTechnician(Long technicianId);
    AssignmentResponseDTO updateAssignmentStatus(Long assignmentId, AssignmentStatus status);
    AssignmentResponseDTO reassignTechnician(Long assignmentId, Long technicianId);
	void deleteAssignment(Long assignmentId);
}