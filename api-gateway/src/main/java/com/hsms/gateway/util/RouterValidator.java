package com.hsms.gateway.util;

import java.util.List;
import java.util.function.Predicate;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

@Component
public class RouterValidator {

    private static final List<String> OPEN_API_ENDPOINTS = List.of(
            "/api/auth/register",
            "/api/auth/login"
    );

    public Predicate<ServerHttpRequest> isSecured =
            request -> OPEN_API_ENDPOINTS.stream()
                    .noneMatch(uri -> request.getURI().getPath().contains(uri));
}