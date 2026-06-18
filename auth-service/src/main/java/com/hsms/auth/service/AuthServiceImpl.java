package com.hsms.auth.service;


import com.hsms.auth.client.UserServiceClient;
import com.hsms.auth.dto.AuthResponse;
import com.hsms.auth.dto.LoginRequest;
import com.hsms.auth.dto.RegisterRequest;
import com.hsms.auth.entity.User;
import com.hsms.auth.repository.UserRepository;
import com.hsms.auth.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final UserServiceClient userServiceClient;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        log.info("Registering user with email: {}", request.getEmail());

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .phoneNumber(request.getPhoneNumber())
                .role(request.getRole())
                .isActive(true)
                .build();

        user = userRepository.save(user);
        log.info("User saved in auth-service: {}", user.getEmail());

        // Sync with user-service to create Customer or Technician record
        try {
            log.info("Syncing user with user-service...");
            com.hsms.auth.dto.UserDTO userDTO = com.hsms.auth.dto.UserDTO.builder()
                    .email(user.getEmail())
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .phoneNumber(user.getPhoneNumber())
                    .role(user.getRole())
                    .isActive(user.getIsActive())
                    .build();
            userServiceClient.syncUser(userDTO);
            log.info("User successfully synced with user-service");
        } catch (Exception e) {
            log.error("Failed to sync user with user-service: {}", e.getMessage());
            // Continue with registration even if sync fails
            // The user-service will sync when the user is first accessed
        }

        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name());

        return AuthResponse.builder()
                .userId(user.getUserId())
                .email(user.getEmail())
                .name(user.getFirstName() + " " + user.getLastName())
                .role(user.getRole().name())
                .success(true)
                .message("Registration successful")
                .accessToken(token)
                .tokenType("Bearer")
                .expiresIn(86400000L)
                .build();
    }

    public AuthResponse login(LoginRequest request) {
        log.info("Login attempt for email: {}", request.getEmail());

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        if (!user.getIsActive()) {
            throw new RuntimeException("User account is inactive");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name());

        return AuthResponse.builder()
                .userId(user.getUserId())
                .email(user.getEmail())
                .name(user.getFirstName() + " " + user.getLastName())
                .role(user.getRole().name())
                .success(true)
                .message("Login successful")
                .accessToken(token)
                .tokenType("Bearer")
                .expiresIn(86400000L)
                .build();
    }

    public Boolean validateToken(String token) {
        return jwtUtil.validateToken(token);
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found: " + email));
    }
}
