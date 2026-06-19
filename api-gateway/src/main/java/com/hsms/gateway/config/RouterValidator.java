package com.hsms.gateway.config;

import java.util.List;
import java.util.function.Predicate;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

@Component
public class RouterValidator {

	public static final List<String> openEndpoints = List.of(

			"/api/auth/register", "/api/auth/login");

	public Predicate<ServerHttpRequest> isSecured = request ->

	openEndpoints.stream().noneMatch(uri -> request.getURI().getPath().contains(uri));
}