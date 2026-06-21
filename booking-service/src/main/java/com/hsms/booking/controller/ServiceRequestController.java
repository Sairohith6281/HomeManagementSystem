package com.hsms.booking.controller;

import com.hsms.booking.dto.ServiceRequestDTO;
import com.hsms.booking.dto.ServiceRequestResponse;
import com.hsms.booking.enums.ServiceRequestStatus;
import com.hsms.booking.service.ServiceRequestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

@RestController
@RequestMapping("/api/service-requests")
@Tag(name = "Service Requests", description = "Service request management endpoints")
public class ServiceRequestController {

	private static final Logger log = LoggerFactory.getLogger(ServiceRequestController.class);
	private final ServiceRequestService serviceRequestService;

	public ServiceRequestController(ServiceRequestService serviceRequestService) {
		this.serviceRequestService = serviceRequestService;
	}

	@PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
	@PostMapping
	@Operation(summary = "Create new service request")
	public ResponseEntity<ServiceRequestResponse> createServiceRequest(@Valid @RequestBody ServiceRequestDTO request,
			@RequestParam Long customerId) {

		log.info("POST /service-requests - Creating new service request for customer: {}", customerId);

		ServiceRequestResponse response = serviceRequestService.createServiceRequest(request, customerId);
		log.info("Service request created successfully with ID: {}", response.getRequestId());

		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
	@GetMapping("/my-requests")
	public ResponseEntity<Page<ServiceRequestResponse>> getMyRequests(

			@RequestHeader("X-User-Id") Long customerId,

			Pageable pageable) {

		return ResponseEntity.ok(serviceRequestService.getMyRequests(customerId, pageable));
	}

	@PreAuthorize("hasAnyAuthority('ADMIN','SERVICE_MANAGER')")
	@GetMapping
	@Operation(summary = "Get all service requests")
	public ResponseEntity<Page<ServiceRequestResponse>> getAllRequests(
			@PageableDefault(size = 10, page = 0, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {

		log.info("GET /service-requests - Fetching all service requests");

		Page<ServiceRequestResponse> requests = serviceRequestService.getAllRequests(pageable);
		log.info("Retrieved {} total service requests", requests.getTotalElements());

		return ResponseEntity.ok(requests);
	}

	@GetMapping("/{id}")
	@Operation(summary = "Get service request by ID")
	public ResponseEntity<ServiceRequestResponse> getRequestById(@PathVariable Long id) {
		log.info("GET /service-requests/{} - Fetching service request", id);

		ServiceRequestResponse response = serviceRequestService.getRequestById(id);
		log.info("Service request retrieved successfully with ID: {}", id);

		return ResponseEntity.ok(response);
	}

	@PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
	@PutMapping("/{id}/cancel")
	public ResponseEntity<ServiceRequestResponse> cancelRequest(

			@PathVariable Long id,

			@RequestHeader("X-User-Id") Long customerId) {

		return ResponseEntity.ok(serviceRequestService.cancelRequest(id, customerId));
	}

	@GetMapping("/status/{status}")
	@Operation(summary = "Get service requests by status")
	public ResponseEntity<Page<ServiceRequestResponse>> getRequestsByStatus(@PathVariable ServiceRequestStatus status,
			@PageableDefault(size = 10, page = 0, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {

		log.info("GET /service-requests/status/{} - Fetching requests with status", status);

		Page<ServiceRequestResponse> requests = serviceRequestService.getRequestsByStatus(status, pageable);
		log.info("Retrieved {} service requests with status: {}", requests.getTotalElements(), status);

		return ResponseEntity.ok(requests);
	}

	@GetMapping("/technician/{technicianId}")
	@Operation(summary = "Get service requests by technician")
	public ResponseEntity<Page<ServiceRequestResponse>> getRequestsByTechnician(@PathVariable Long technicianId,
			@PageableDefault(size = 10, page = 0, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {

		log.info("GET /service-requests/technician/{} - Fetching requests for technician", technicianId);

		Page<ServiceRequestResponse> requests = serviceRequestService.getRequestsByTechnician(technicianId, pageable);
		log.info("Retrieved {} service requests for technician {}", requests.getTotalElements(), technicianId);

		return ResponseEntity.ok(requests);
	}

	@GetMapping("/category/{categoryId}")
	@Operation(summary = "Get service requests by category")
	public ResponseEntity<Page<ServiceRequestResponse>> getRequestsByCategory(@PathVariable Long categoryId,
			@PageableDefault(size = 10, page = 0, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {

		log.info("GET /service-requests/category/{} - Fetching requests for category", categoryId);

		Page<ServiceRequestResponse> requests = serviceRequestService.getRequestsByCategory(categoryId, pageable);
		log.info("Retrieved {} service requests for category {}", requests.getTotalElements(), categoryId);

		return ResponseEntity.ok(requests);
	}

	@PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_SERVICE_MANAGER')")
	@PutMapping("/{id}/status")
	@Operation(summary = "Update service request status")
	public ResponseEntity<ServiceRequestResponse> updateServiceRequestStatus(@PathVariable Long id,
			@RequestParam String status) {

		log.info("PUT /service-requests/{}/status - Updating status to {}", id, status);

		ServiceRequestResponse response = serviceRequestService.updateStatus(id, status);
		log.info("Service request status updated successfully with ID: {}", id);

		return ResponseEntity.ok(response);
	}

	@GetMapping("/summary")
	@Operation(summary = "Get service requests summary for reporting")
	public ResponseEntity<List<com.hsms.booking.dto.ServiceRequestSummary>> getAllServiceRequestsSummary() {
		log.info("GET /service-requests/summary - Fetching service requests summary");

		List<com.hsms.booking.dto.ServiceRequestSummary> summary = serviceRequestService.getAllSummary();
		log.info("Retrieved {} service request summaries", summary.size());

		return ResponseEntity.ok(summary);
	}
}
