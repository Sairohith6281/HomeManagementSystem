package com.hsms.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.hsms.model.ServiceRequestDTO;

@FeignClient(name = "service-request-service")
public interface ServiceRequestClient {
    @GetMapping("/api/requests/{id}")
    ResponseEntity<ServiceRequestDTO> getServiceRequestById(@PathVariable Long id);
}
