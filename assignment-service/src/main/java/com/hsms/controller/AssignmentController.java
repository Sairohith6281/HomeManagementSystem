package com.hsms.controller;

import org.springframework.web.bind.annotation.RestController;
import com.hsms.entity.AssignmentStatus;
import com.hsms.model.AssignmentDetailResponseDTO;
import com.hsms.model.AssignmentRequestDTO;
import com.hsms.model.AssignmentResponseDTO;
import com.hsms.service.AssignmentService;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/assignments")
@RequiredArgsConstructor
public class AssignmentController {

	private final AssignmentService assignmentService;

	@PostMapping
	public ResponseEntity<AssignmentResponseDTO> assignTechnician(@RequestBody AssignmentRequestDTO requestDTO) {
		return ResponseEntity.ok(assignmentService.assignTechnician(requestDTO));
	}

	@GetMapping("/{id}")
	public ResponseEntity<AssignmentDetailResponseDTO> getAssignmentById(@PathVariable Long id) {
		return ResponseEntity.ok(assignmentService.getAssignmentById(id));
	}

	@GetMapping
	public ResponseEntity<List<AssignmentDetailResponseDTO>> getAllAssignments() {
		return ResponseEntity.ok(assignmentService.getAllAssignments());
	}

	@GetMapping("/technician/{technicianId}")
	public ResponseEntity<List<AssignmentResponseDTO>> getAssignmentsByTechnician(@PathVariable Long technicianId) {
		return ResponseEntity.ok(assignmentService.getAssignmentsByTechnician(technicianId));
	}

	@PutMapping("/{id}/status")
	public ResponseEntity<AssignmentResponseDTO> updateAssignmentStatus(@PathVariable Long id,
			@RequestParam AssignmentStatus status) {
		return ResponseEntity.ok(assignmentService.updateAssignmentStatus(id, status));
	}

	@PutMapping("/{id}/reassign")
	public ResponseEntity<AssignmentResponseDTO> reassignTechnician(@PathVariable Long id,
			@RequestParam Long technicianId) {
		return ResponseEntity.ok(assignmentService.reassignTechnician(id, technicianId));
	}
}
