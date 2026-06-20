package com.hsms.execution_service.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.hsms.execution_service.model.ServiceRecordRequestDTO;

@FeignClient(name = "booking-service")
public interface BookingserviceClient {
	
    @GetMapping("/api/requests/{id}")
    ServiceRecordRequestDTO getRequest(@PathVariable Long id);
    
    @PutMapping("/api/booking-requests/{id}/status")
    void updateStatus(@PathVariable Long id, @RequestParam String status);
}