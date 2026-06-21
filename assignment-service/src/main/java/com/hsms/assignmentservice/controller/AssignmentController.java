package com.hsms.assignmentservice.controller;

import com.hsms.model.AssignmentDetailResponseDTO;
import com.hsms.model.AssignmentRequestDTO;
import com.hsms.model.AssignmentResponseDTO;
import com.hsms.service.AssignmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/assignments")
@RequiredArgsConstructor
public class AssignmentController {

    private final AssignmentService assignmentService;

    @PostMapping
    public ResponseEntity<AssignmentResponseDTO> assignTechnician(@RequestBody AssignmentRequestDTO requestDTO) {
        return ResponseEntity.ok(assignmentService.assignTechnician(requestDTO));
    }

    @PutMapping("/{id}/accept")
    public ResponseEntity<AssignmentResponseDTO> acceptJob(@PathVariable Long id) {
        return ResponseEntity.ok(assignmentService.acceptJob(id));
    }

    @PutMapping("/{id}/reject")
    public ResponseEntity<AssignmentResponseDTO> rejectJob(@PathVariable Long id) {
        return ResponseEntity.ok(assignmentService.rejectJob(id));
    }

    @PutMapping("/{id}/reassign")
    public ResponseEntity<AssignmentResponseDTO> reassignTechnician(
            @PathVariable Long id,
            @RequestParam Long technicianId,
            @RequestParam LocalDateTime startTime) {
        return ResponseEntity.ok(assignmentService.reassignTechnician(id, technicianId, startTime));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAssignment(@PathVariable Long id) {
        assignmentService.deleteAssignment(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<AssignmentDetailResponseDTO>> getAllAssignments() {
        return ResponseEntity.ok(assignmentService.getAllAssignments());
    }
}
