package com.hsms.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.hsms.model.TechnicianDetailResponseDTO;

@FeignClient(name = "user-service", fallback = UserServiceClientFallback.class)
public interface UserServiceClient {

    @GetMapping("/api/technicians/{userId}")
    ResponseEntity<TechnicianDetailResponseDTO> getTechnicianById(@PathVariable("userId") Long userId);
}