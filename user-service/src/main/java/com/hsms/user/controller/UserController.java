package com.hsms.user.controller;

import com.hsms.user.dto.CustomerDTO;
import com.hsms.user.dto.TechnicianDTO;
import com.hsms.user.dto.UserDTO;
import com.hsms.user.entity.User;
import com.hsms.user.service.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        log.info("GET /{} - Fetching user by ID", id);
        UserDTO user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<UserDTO> getUserByEmail(@PathVariable String email) {
        log.info("GET /email/{} - Fetching user by email", email);
        UserDTO user = userService.getUserByEmail(email);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/role/{role}")
    public ResponseEntity<List<UserDTO>> getUsersByRole(@PathVariable User.Role role) {
        log.info("GET /role/{} - Fetching users by role", role);
        List<UserDTO> users = userService.getUsersByRole(role);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/technicians/active")
    public ResponseEntity<List<UserDTO>> getActiveTechnicians() {
        log.info("GET /technicians/active - Fetching active technicians");
        List<UserDTO> technicians = userService.getActiveTechnicians();
        return ResponseEntity.ok(technicians);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @Valid @RequestBody UserDTO userDTO) {
        log.info("PUT /{} - Updating user", id);
        UserDTO updatedUser = userService.updateUser(id, userDTO);
        return ResponseEntity.ok(updatedUser);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<UserDTO> toggleUserStatus(@PathVariable Long id, @RequestParam Boolean isActive) {
        log.info("PUT /{}/status - Toggling user status to {}", id, isActive);
        UserDTO updatedUser = userService.toggleUserStatus(id, isActive);
        return ResponseEntity.ok(updatedUser);
    }

    @PostMapping("/sync")
    public ResponseEntity<UserDTO> syncUser(@Valid @RequestBody UserDTO userDTO) {
        log.info("POST /sync - Syncing user: {}", userDTO.getEmail());
        UserDTO syncedUser = userService.syncUser(userDTO);
        return ResponseEntity.ok(syncedUser);
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("User Service is UP");
    }

    // ========== TECHNICIAN ENDPOINTS ==========

    @GetMapping("/technicians/{id}")
    public ResponseEntity<TechnicianDTO> getTechnicianById(@PathVariable Long id) {
        log.info("GET /technicians/{} - Fetching technician", id);
        TechnicianDTO technician = userService.getTechnicianById(id);
        return ResponseEntity.ok(technician);
    }

    @GetMapping("/technicians/all")
    public ResponseEntity<List<TechnicianDTO>> getAllTechnicians() {
        log.info("GET /technicians/all - Fetching all technicians");
        List<TechnicianDTO> technicians = userService.getAllTechnicians();
        return ResponseEntity.ok(technicians);
    }

    // ========== CUSTOMER ENDPOINTS ==========

    @GetMapping("/customers/{id}")
    public ResponseEntity<CustomerDTO> getCustomerById(@PathVariable Long id) {
        log.info("GET /customers/{} - Fetching customer", id);
        CustomerDTO customer = userService.getCustomerById(id);
        return ResponseEntity.ok(customer);
    }

    @GetMapping("/customers/{id}/exists")
    public ResponseEntity<Boolean> customerExists(@PathVariable Long id) {
        log.info("GET /customers/{}/exists - Checking customer existence", id);
        boolean exists = userService.customerExists(id);
        return ResponseEntity.ok(exists);
    }

    @GetMapping("/customers/all")
    public ResponseEntity<List<CustomerDTO>> getAllCustomers() {
        log.info("GET /customers/all - Fetching all customers");
        List<CustomerDTO> customers = userService.getAllCustomers();
        return ResponseEntity.ok(customers);
    }
}
