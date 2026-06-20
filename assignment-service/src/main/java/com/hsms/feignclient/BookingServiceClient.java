package com.hsms.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.hsms.model.ServiceRequestDTO;

@FeignClient(name = "booking-service")
public interface BookingServiceClient {
    @GetMapping("/api/requests/{id}")
    ResponseEntity<ServiceRequestDTO> getServiceRequestById(@PathVariable Long id);
    @PutMapping("/api/service-requests/{id}/status")
    void updateStatus(@PathVariable Long id, @RequestParam String status);
}
