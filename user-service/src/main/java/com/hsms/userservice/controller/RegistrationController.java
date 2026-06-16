package com.hsms.userservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hsms.userservice.dto.CustomerRegistrationDTO;
import com.hsms.userservice.dto.TechnicianRegistrationDTO;
import com.hsms.userservice.dto.UserRegistrationDTO;
import com.hsms.userservice.service.RegistrationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/register")
@RequiredArgsConstructor
public class RegistrationController  {
	
    private final RegistrationService registrationService;

    @PostMapping("/user")
    public ResponseEntity<String> registerUser(
            @RequestBody UserRegistrationDTO dto) {

        return ResponseEntity.ok(
                registrationService.registerUser(dto));
    }

    @PostMapping("/customer")
    public ResponseEntity<String> registerCustomer(
            @RequestBody CustomerRegistrationDTO dto) {

        return ResponseEntity.ok(
                registrationService.registerCustomer(dto));
    }

    @PostMapping("/technician")
    public ResponseEntity<String> registerTechnician(
            @RequestBody TechnicianRegistrationDTO dto) {

        return ResponseEntity.ok(
                registrationService.registerTechnician(dto));
    }

}
