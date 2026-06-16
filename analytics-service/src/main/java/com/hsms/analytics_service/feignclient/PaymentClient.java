package com.hsms.analytics_service.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "payment-service")
public interface PaymentClient {
	
    @GetMapping("/api/payments/revenue")
    double getTotalRevenue();
}