package com.hsms.auth.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hsms.auth.dto.AuthResponse;
import com.hsms.auth.dto.LoginRequest;
import com.hsms.auth.dto.RegisterRequest;
import com.hsms.auth.service.AuthService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        log.info("POST /register - Registering user: {}", request.getEmail());
        AuthResponse response = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        log.info("POST /login - Login attempt: {}", request.getEmail());
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/validate")
    public ResponseEntity<Boolean> validateToken(@RequestParam String token) {
        Boolean isValid = authService.validateToken(token);
        return ResponseEntity.ok(isValid);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<com.hsms.auth.dto.UserDTO> getUserByEmail(@PathVariable String email) {
        log.info("GET /email/{} - Fetching user info", email);
        com.hsms.auth.entity.User user = authService.getUserByEmail(email);
        return ResponseEntity.ok(convertToDTO(user));
    }

    private com.hsms.auth.dto.UserDTO convertToDTO(com.hsms.auth.entity.User user) {
        return com.hsms.auth.dto.UserDTO.builder()
                .id(user.getUserId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phoneNumber(user.getPhoneNumber())
                .role(user.getRole())
                .isActive(user.getIsActive())
                .build();
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Auth Service is UP");
    }
}
