package com.hsms.analytics_service.feignclient;

import java.util.Map;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "category-service")
public interface CategoryClient {
	
    @GetMapping("/api/categories/distribution")
    Map<Long, Integer> getCategoryDistribution();
}