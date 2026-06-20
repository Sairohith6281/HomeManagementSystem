package com.hsms.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.hsms.model.TechnicianDetailResponseDTO;

@FeignClient(name = "technician-service")
public interface TechnicianClient {
    @GetMapping("/api/technicians/{id}")
    ResponseEntity<TechnicianDetailResponseDTO> getTechnicianById(@PathVariable Long id);
}