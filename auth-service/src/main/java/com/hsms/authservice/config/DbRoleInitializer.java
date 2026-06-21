package com.hsms.authservice.config;

import com.hsms.authservice.entity.Role;
import com.hsms.authservice.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DbRoleInitializer implements CommandLineRunner {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public void run(String... args) throws Exception {
        List<String> defaultRoles = List.of("CUSTOMER", "ADMIN", "TECHNICIAN", "SERVICE_MANAGER");

        for (String roleName : defaultRoles) {
            if (!roleRepository.findByRoleName(roleName).isPresent()) {
                Role role = new Role();
                role.setRoleName(roleName);
                roleRepository.save(role);
                System.out.println("Seeded role: " + roleName);
            }
        }
    }
}
