package com.hsms.userservice.security;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class HeaderAuthenticationFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String email = request.getHeader("X-User-Email");
		String role = request.getHeader("X-User-Role");

		System.out.println("user-service received X-User-Email: " + email);
		System.out.println("user-service received X-User-Role: " + role);

		if (email != null && role != null) {
			String roleName = role.toUpperCase();
//			if (roleName.startsWith("")) {
//				roleName = roleName.substring(5);
//			}

			UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(email, null,
					List.of(new SimpleGrantedAuthority("ROLE_" + roleName)));

			SecurityContextHolder.getContext().setAuthentication(authentication);
			System.out.println("user-service successfully set SecurityContext with authority: ROLE_" + roleName);
		} else {
			System.out.println("user-service: email or role was NULL!");
		}

		filterChain.doFilter(request, response);
	}
}