package com.hsms.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * API Gateway - Single Entry Point for All Microservices
 * Routes requests to appropriate services via Eureka
 */
@SpringBootApplication
@EnableDiscoveryClient
public class ApiGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApplication.class, args);
        System.out.println("===========================================");
        System.out.println("API Gateway Started Successfully!");
        System.out.println("Gateway URL: http://localhost:8080");
        System.out.println("===========================================");
    }
}
