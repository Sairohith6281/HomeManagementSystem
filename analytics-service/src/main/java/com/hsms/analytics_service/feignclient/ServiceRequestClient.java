package com.hsms.analytics_service.feignclient;

import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import com.hsms.analytics_service.model.ServiceRequestDetailResponseDTO;

@FeignClient(name = "service-request-service")
public interface ServiceRequestClient {
	
    @GetMapping("/api/requests")
    List<ServiceRequestDetailResponseDTO> getAllRequests();
}