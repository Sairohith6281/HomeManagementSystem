package com.hsms.authservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hsms.authservice.entity.User;
import com.hsms.authservice.model.LoginRequestDTO;
import com.hsms.authservice.model.LoginResponseDTO;
import com.hsms.authservice.model.RegisterRequestDTO;
import com.hsms.authservice.model.RegisterResponseDTO;
import com.hsms.authservice.model.UserProfileDTO;
import com.hsms.authservice.repository.UserRepository;
import com.hsms.authservice.service.AuthService;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService service;
    
    @Autowired
    private UserRepository userRepository;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDTO> register(@Valid @RequestBody RegisterRequestDTO dto){

        return new ResponseEntity<>(service.register(dto),HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO dto){

        return ResponseEntity.ok(service.login(dto));
    }
    
    @GetMapping("/profile")
    public UserProfileDTO getProfile(Authentication authentication) {

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->new RuntimeException("User not found"));

        UserProfileDTO dto = new UserProfileDTO();

        dto.setUserId(user.getUserId());
        dto.setName(user.getFirstName()+user.getLastName());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRoles());

        return dto;
    }
    
    @GetMapping("/users/{id}")
    public UserProfileDTO getUserById(@PathVariable Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("User Not Found"));

        UserProfileDTO dto = new UserProfileDTO();

        dto.setUserId(user.getUserId());
        dto.setName(
                user.getFirstName() + " " + user.getLastName());

        dto.setEmail(user.getEmail());
        dto.setRole(user.getRoles());

        return dto;
    }
}