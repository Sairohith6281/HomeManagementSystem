package com.hsms.analytics_service.feignclient;

import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import com.hsms.analytics_service.model.ServiceRequestDetailResponseDTO;

@FeignClient(name = "booking-service")
public interface bookingServiceClient {

    @GetMapping("/api/requests")
    List<ServiceRequestDetailResponseDTO> getAllRequests();
}