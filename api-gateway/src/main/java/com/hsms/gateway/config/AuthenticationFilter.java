package com.hsms.gateway.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;


@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    @Autowired
    private RouterValidator validator;

    @Autowired
    private JwtUtil jwtUtil;

    public AuthenticationFilter() {
        super(Config.class);
    }

    public static class Config {
    }

    @Override
    public GatewayFilter apply(Config config) {

        return (exchange, chain) -> {

            if (validator.isSecured.test(exchange.getRequest())) {

                // Check Authorization Header
                if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                    exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                    return exchange.getResponse().setComplete();
                }

                String authHeader =
                        exchange.getRequest().getHeaders()
                                .getFirst(HttpHeaders.AUTHORIZATION);

                if (authHeader != null && authHeader.startsWith("Bearer ")) {
                    authHeader = authHeader.substring(7);
                }

                try {
                    jwtUtil.validateToken(authHeader);
                } catch (Exception ex) {
                    exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                    return exchange.getResponse().setComplete();
                }

                Long userId = jwtUtil.extractUserId(authHeader);
                String email = jwtUtil.extractUsername(authHeader);
                String role = jwtUtil.extractRole(authHeader).toUpperCase();

                String path = exchange.getRequest().getURI().getPath();
                String method = exchange.getRequest().getMethod().name();
                
                
                System.out.println("Username:" + userId);
                System.out.println("email:"  + email);
                System.out.println("Path = " + path);
                System.out.println("Method = " + method);
                System.out.println("Role = " + role);

                // ===============================
                // CUSTOMER APIs
                // ===============================
                if (path.startsWith("/api/customers")) {

                    if (!(role.equals("CUSTOMER")
                            || role.equals("ADMIN")
                            || role.equals("SERVICE_MANAGER"))) {

                        exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                        return exchange.getResponse().setComplete();
                    }
                }

                // ===============================
                // TECHNICIAN APIs
                // ===============================
                if (path.startsWith("/api/technicians")) {

                    // Create Technician
                    if (HttpMethod.POST.matches(method)) {

                        if (!(role.equals("ADMIN")
                                || role.equals("SERVICE_MANAGER"))) {

                            exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                            return exchange.getResponse().setComplete();
                        }
                    }

                    // View Technicians
                    if (HttpMethod.GET.matches(method)) {

                        if (!(role.equals("ADMIN")
                                || role.equals("SERVICE_MANAGER")
                                || role.equals("CUSTOMER")
                                || role.equals("TECHNICIAN"))) {

                            exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                            return exchange.getResponse().setComplete();
                        }
                    }

                    // Update Technician
                    if (HttpMethod.PUT.matches(method)) {

                        if (!(role.equals("ADMIN")
                                || role.equals("SERVICE_MANAGER"))) {

                            exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                            return exchange.getResponse().setComplete();
                        }
                    }

                    // Delete Technician
                    if (HttpMethod.DELETE.matches(method)) {

                        if (!(role.equals("ADMIN"))) {

                            exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                            return exchange.getResponse().setComplete();
                        }
                    }
                }

                // ===============================
                // SERVICE REQUEST APIs
                // ===============================
                if (path.startsWith("/api/service-requests")) {

                    if (HttpMethod.POST.matches(method)) {

                        if (!(role.equals("CUSTOMER")
                                || role.equals("ADMIN"))) {

                            exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                            return exchange.getResponse().setComplete();
                        }
                    }

                    if (HttpMethod.GET.matches(method)) {

                        if (!(role.equals("CUSTOMER")
                                || role.equals("TECHNICIAN")
                                || role.equals("ADMIN")
                                || role.equals("SERVICE_MANAGER"))) {

                            exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                            return exchange.getResponse().setComplete();
                        }
                    }

                    if (HttpMethod.PUT.matches(method)) {

                        if (!(role.equals("TECHNICIAN")
                                || role.equals("ADMIN")
                                || role.equals("SERVICE_MANAGER"))) {

                            exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                            return exchange.getResponse().setComplete();
                        }
                    }
                }
                
                // Add User Info Headers
                exchange = exchange.mutate()
                        .request(exchange.getRequest().mutate()
                                .header("X-User-Id", String.valueOf(userId))
                                .header("X-User-Email", email)
                                .header("X-User-Role", role)
                                .build())
                        .build();
            }

            return chain.filter(exchange);
        };
    }
}