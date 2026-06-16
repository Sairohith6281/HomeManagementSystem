package com.hsms.service;

import java.time.LocalDateTime;
import java.util.List;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import com.hsms.entity.Assignment;
import com.hsms.entity.AssignmentStatus;
import com.hsms.model.AssignmentDetailResponseDTO;
import com.hsms.model.AssignmentRequestDTO;
import com.hsms.model.AssignmentResponseDTO;
import com.hsms.repository.AssignmentRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AssignmentServiceImpl implements AssignmentService {

	private final AssignmentRepository assignmentRepository;
	private final ModelMapper modelMapper;

	@Override
	public AssignmentResponseDTO assignTechnician(AssignmentRequestDTO requestDTO) {
		Assignment assignment = new Assignment();
		assignment.setTechnicianId(requestDTO.getTechnicianId());
		assignment.setServiceRequestId(requestDTO.getServiceRequestId());
		assignment.setAssignedDate(LocalDateTime.now());
		assignment.setStatus(AssignmentStatus.ASSIGNED);
		Assignment saved = assignmentRepository.save(assignment);
		return modelMapper.map(saved, AssignmentResponseDTO.class);
	}

	@Override
	public AssignmentDetailResponseDTO getAssignmentById(Long assignmentId) {
		Assignment assignment = assignmentRepository.findById(assignmentId)
				.orElseThrow(() -> new RuntimeException("Assignment not found"));
		return modelMapper.map(assignment, AssignmentDetailResponseDTO.class);
	}

	@Override
	public List<AssignmentDetailResponseDTO> getAllAssignments() {
		return assignmentRepository.findAll().stream().map(a -> modelMapper.map(a, AssignmentDetailResponseDTO.class))
				.toList();
	}

	@Override
	public List<AssignmentResponseDTO> getAssignmentsByTechnician(Long technicianId) {
		return assignmentRepository.findAll().stream().filter(a -> a.getTechnicianId().equals(technicianId))
				.map(a -> modelMapper.map(a, AssignmentResponseDTO.class)).toList();
	}

	@Override
	public AssignmentResponseDTO updateAssignmentStatus(Long assignmentId, AssignmentStatus status) {
		Assignment assignment = assignmentRepository.findById(assignmentId)
				.orElseThrow(() -> new RuntimeException("Assignment not found"));
		assignment.setStatus(status);
		Assignment updated = assignmentRepository.save(assignment);
		return modelMapper.map(updated, AssignmentResponseDTO.class);
	}

	@Override
	public AssignmentResponseDTO reassignTechnician(Long assignmentId, Long technicianId) {
		Assignment assignment = assignmentRepository.findById(assignmentId)
				.orElseThrow(() -> new RuntimeException("Assignment not found"));
		assignment.setTechnicianId(technicianId);
		assignment.setStatus(AssignmentStatus.REASSIGNED);
		Assignment updated = assignmentRepository.save(assignment);
		return modelMapper.map(updated, AssignmentResponseDTO.class);
	}
}
