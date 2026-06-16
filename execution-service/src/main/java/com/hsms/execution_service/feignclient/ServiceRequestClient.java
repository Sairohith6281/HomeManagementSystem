package com.hsms.execution_service.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.hsms.execution_service.model.ServiceRecordRequestDTO;

@FeignClient(name = "service-request-service")
public interface ServiceRequestClient {
	
    @GetMapping("/api/requests/{id}")
    ServiceRecordRequestDTO getRequest(@PathVariable Long id);
}