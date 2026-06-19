package com.hsms.analytics_service.feignclient;

import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import com.hsms.analytics_service.model.TechnicianDetailResponseDTO;

@FeignClient(name = "technician-service")
public interface TechnicianClient {

    @GetMapping("/api/technicians")
    List<TechnicianDetailResponseDTO> getAllTechnicians();
}