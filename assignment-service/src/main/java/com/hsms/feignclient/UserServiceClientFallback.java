package com.hsms.feignclient;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.hsms.model.TechnicianDetailResponseDTO;

@Component
public class UserServiceClientFallback implements UserServiceClient {

    @Override
    public ResponseEntity<TechnicianDetailResponseDTO> getTechnicianById(Long userId) {
    	
    	TechnicianDetailResponseDTO fallbackDto = new TechnicianDetailResponseDTO();
        fallbackDto.setUserId(userId);
        fallbackDto.setSkill("Unavailable");
        fallbackDto.setAvailability("Unknown");
        return ResponseEntity.ok(fallbackDto);
    }
}