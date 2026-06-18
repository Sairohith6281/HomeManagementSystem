package com.hsms.booking.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Feign client for User Service
 */
@FeignClient(name = "user-service")
public interface UserClient {
    
    @GetMapping("/api/users/customers/{id}")
    CustomerDTO getCustomerById(@PathVariable("id") Long id);
    
    @GetMapping("/api/users/customers/{id}/exists")
    Boolean customerExists(@PathVariable("id") Long id);
}
