package com.hsms.userservice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hsms.userservice.model.TechnicianDetailResponseDTO;
import com.hsms.userservice.model.TechnicianProfileRequestDTO;
import com.hsms.userservice.security.RoleValidator;
import com.hsms.userservice.security.Roles;
import com.hsms.userservice.service.UserService;

@RestController
@RequestMapping("/api/technicians")
public class TechnicianController {

	@Autowired
	private UserService service;

	/*
	 * @PostMapping public ResponseEntity<TechnicianDetailResponseDTO>
	 * createTechnician(
	 * 
	 * @RequestBody TechnicianProfileRequestDTO dto) {
	 * 
	 * return new ResponseEntity<>( service.createTechnician(dto),
	 * HttpStatus.CREATED); }
	 */

	@PreAuthorize("hasAnyRole('TECHNICIAN','ADMIN')")
	@PostMapping
	public ResponseEntity<TechnicianDetailResponseDTO> createTechnician(

			@RequestHeader("X-User-Role") String role,

			@RequestBody TechnicianProfileRequestDTO dto) {
		
//		RoleValidator.validate(role, Roles.TECHNICIAN);
//		RoleValidator.validate(role, Roles.ADMIN);

		return new ResponseEntity<>(service.createTechnician(dto), HttpStatus.CREATED);
	}

	@PutMapping("/{userId}")
	public ResponseEntity<TechnicianDetailResponseDTO> updateTechnician(@PathVariable Long userId,
			@RequestBody TechnicianProfileRequestDTO dto) {

		return ResponseEntity.ok(service.updateTechnician(userId, dto));
	}

	@GetMapping("/{userId}")
	public ResponseEntity<TechnicianDetailResponseDTO> getTechnician(@PathVariable Long userId) {

		return ResponseEntity.ok(service.getTechnician(userId));
	}

	/*
	 * @GetMapping public ResponseEntity<List<TechnicianDetailResponseDTO>>
	 * getAllTechnicians() {
	 * 
	 * return ResponseEntity.ok(service.getAllTechnicians()); }
	 */

	@PreAuthorize("hasAnyRole('ADMIN','SERVICE_MANAGER')")
	@GetMapping
	public ResponseEntity<List<TechnicianDetailResponseDTO>> getAllTechnicians(

			@RequestHeader("X-User-Role") String role) {

		RoleValidator.validate(role, Roles.ADMIN, Roles.SERVICE_MANAGER);

		return ResponseEntity.ok(service.getAllTechnicians());
	}

	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/{userId}")
	public ResponseEntity<String> deleteTechnician(@PathVariable Long userId) {

		service.deleteTechnician(userId);

		return ResponseEntity.ok("Technician Deleted Successfully");
	}

	@GetMapping("/technicianId/{technicianId}")
	public ResponseEntity<TechnicianDetailResponseDTO> getTechnicianById(@PathVariable Long technicianId) {

		return ResponseEntity.ok(service.getTechnicianById(technicianId));
	}
}