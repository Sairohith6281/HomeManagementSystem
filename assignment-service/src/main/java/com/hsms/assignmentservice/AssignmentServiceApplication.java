package com.hsms.assignmentservice;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.hsms")
@EnableFeignClients(basePackages = "com.hsms")
@EnableJpaRepositories(basePackages = "com.hsms.repository")
@EntityScan(basePackages = "com.hsms.entity")
public class AssignmentServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(AssignmentServiceApplication.class, args);
	}

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}
}
