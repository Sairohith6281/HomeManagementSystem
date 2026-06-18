package com.hsms.auth.service;


import com.hsms.auth.dto.AuthResponse;
import com.hsms.auth.dto.LoginRequest;
import com.hsms.auth.dto.RegisterRequest;
import com.hsms.auth.entity.User;

public interface AuthService {

    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);

    Boolean validateToken(String token);

    User getUserByEmail(String email);
}