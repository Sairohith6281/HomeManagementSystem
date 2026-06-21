package com.hsms.booking.security;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
public class FeignClientInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            String userId = request.getHeader("X-User-Id");
            String email = request.getHeader("X-User-Email");
            String role = request.getHeader("X-User-Role");

            if (userId != null) {
                System.out.println("booking-service Feign interceptor forwarding X-User-Id: " + userId);
                template.header("X-User-Id", userId);
            }
            if (email != null) {
                System.out.println("booking-service Feign interceptor forwarding X-User-Email: " + email);
                template.header("X-User-Email", email);
            }
            if (role != null) {
                System.out.println("booking-service Feign interceptor forwarding X-User-Role: " + role);
                template.header("X-User-Role", role);
            }
        } else {
            System.out.println("booking-service Feign interceptor: RequestContextAttributes is NULL!");
        }
    }
}
