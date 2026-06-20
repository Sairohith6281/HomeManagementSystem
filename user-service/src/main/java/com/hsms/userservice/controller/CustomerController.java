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

import com.hsms.userservice.model.CustomerDetailResponseDTO;
import com.hsms.userservice.model.CustomerProfileRequestDTO;
import com.hsms.userservice.security.RoleValidator;
import com.hsms.userservice.security.Roles;
import com.hsms.userservice.service.UserService;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

	@Autowired
	private UserService service;

	@PreAuthorize("hasAnyRole('CUSTOMER','ADMIN')")
	@PostMapping
	public ResponseEntity<CustomerDetailResponseDTO> createCustomer(

			@RequestHeader("X-User-Role") String role,

			@RequestHeader("X-User-Id") Long userId,

			@RequestHeader("X-User-Email") String email,

			@RequestBody CustomerProfileRequestDTO dto) {
		System.out.println(role);

		RoleValidator.validate(role, Roles.CUSTOMER, Roles.ADMIN);

		return new ResponseEntity<>(service.createCustomer(dto, userId, email), HttpStatus.CREATED);
	}

	@PutMapping("/{userId}")
	public ResponseEntity<CustomerDetailResponseDTO> updateCustomer(@PathVariable Long userId,
			@RequestBody CustomerProfileRequestDTO dto) {

		return ResponseEntity.ok(service.updateCustomer(userId, dto));
	}

	@GetMapping("/{userId}")
	public ResponseEntity<CustomerDetailResponseDTO> getCustomer(@PathVariable Long userId) {

		return ResponseEntity.ok(service.getCustomer(userId));
	}

//    @GetMapping
//    public ResponseEntity<List<CustomerDetailResponseDTO>> getAllCustomers() {
//
//        return ResponseEntity.ok(
//                service.getAllCustomers());
//    }

	@PreAuthorize("hasAnyRole('ADMIN','SERVICE_MANAGER')")
	@GetMapping
	public ResponseEntity<List<CustomerDetailResponseDTO>> getAllCustomers(

			@RequestHeader("X-User-Role") String role) {

		RoleValidator.validate(role, Roles.ADMIN, Roles.SERVICE_MANAGER);

		return ResponseEntity.ok(service.getAllCustomers());
	}

	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/{userId}")
	public ResponseEntity<String> deleteCustomer(@PathVariable Long userId) {

		service.deleteCustomer(userId);

		return ResponseEntity.ok("Customer Deleted Successfully");
	}

	@GetMapping("/customerId/{customerId}")
	public ResponseEntity<CustomerDetailResponseDTO> getCustomerById(@PathVariable Long customerId) {

		return ResponseEntity.ok(service.getCustomerById(customerId));
	}
}