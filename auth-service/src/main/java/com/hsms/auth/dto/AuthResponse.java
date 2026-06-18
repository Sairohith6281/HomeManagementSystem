package com.hsms.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResponse {
    private Long userId;
    private String email;
    private String name;
    private String role;
    private Boolean success;
    private String message;
    private String accessToken;
    private String tokenType;
    private Long expiresIn;
}
