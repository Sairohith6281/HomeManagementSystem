package com.hsms.user.client;

import com.hsms.user.dto.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "auth-service")
public interface AuthClient {
    @GetMapping("/api/auth/email/{email}")
    UserDTO getUserByEmail(@PathVariable("email") String email);
}
