package com.hsms.auth.client;

import com.hsms.auth.dto.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Feign client for User Service communication
 */
@FeignClient(name = "user-service")
public interface UserServiceClient {
    
    @PostMapping("/api/users/sync")
    UserDTO syncUser(@RequestBody UserDTO userDTO);
}
