package com.hsms.booking.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import org.springframework.security.web.SecurityFilterChain;

import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

	private final HeaderAuthenticationFilter filter;

	public SecurityConfig(HeaderAuthenticationFilter filter) {

		this.filter = filter;
	}

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		http.csrf(csrf -> csrf.disable())

				.authorizeHttpRequests(auth -> auth.anyRequest().authenticated())

				.anonymous(a -> a.disable())

				.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}
}