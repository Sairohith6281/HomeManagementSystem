package com.hsms.execution_service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.hsms.execution_service.model.ServiceRecordDetailResponseDTO;
import com.hsms.execution_service.model.ServiceRecordRequestDTO;
import com.hsms.execution_service.model.ServiceRecordResponseDTO;
import com.hsms.execution_service.service.ServiceRecordService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/records")
public class ServiceRecordController {

	private final ServiceRecordService service;

	@PostMapping("/start")
	public ResponseEntity<ServiceRecordResponseDTO> start(@RequestBody ServiceRecordRequestDTO dto) {
		return ResponseEntity.ok(service.start(dto));
	}

	@PutMapping("/complete/{id}")
	public ResponseEntity<ServiceRecordDetailResponseDTO> complete(@PathVariable Long id,
			@RequestBody ServiceRecordRequestDTO dto) {
		return ResponseEntity.ok(service.complete(id, dto));
	}

	@GetMapping("/{id}")
	public ResponseEntity<ServiceRecordDetailResponseDTO> get(@PathVariable Long id) {
		return ResponseEntity.ok(service.get(id));
	}
}