package com.hsms.user.service;

import com.hsms.user.dto.CustomerDTO;
import com.hsms.user.dto.TechnicianDTO;
import com.hsms.user.dto.UserDTO;
import com.hsms.user.entity.Customer;
import com.hsms.user.entity.Technician;
import com.hsms.user.entity.User;
import com.hsms.user.repository.CustomerRepository;
import com.hsms.user.repository.TechnicianRepository;
import com.hsms.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final TechnicianRepository technicianRepository;
    private final CustomerRepository customerRepository;
    private final com.hsms.user.client.AuthClient authClient;

    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return convertToDTO(user);
    }

    public UserDTO getUserByEmail(String email) {
        log.info("Fetching user by email: {}", email);
        return userRepository.findByEmail(email)
                .map(this::convertToDTO)
                .orElseGet(() -> {
                    log.info("User {} not found in user-service, checking auth-service", email);
                    try {
                        UserDTO authUser = authClient.getUserByEmail(email);
                        if (authUser != null) {
                            log.info("User found in auth-service, synchronizing to user-service");
                            return syncUser(authUser);
                        }
                    } catch (Exception e) {
                        log.error("Failed to fetch user from auth-service: {}", e.getMessage());
                    }
                    throw new RuntimeException("User not found: " + email);
                });
    }

    @Transactional
    public UserDTO syncUser(UserDTO userDTO) {
        log.info("Syncing user: {} with role: {}", userDTO.getEmail(), userDTO.getRole());
        
        User user = userRepository.findByEmail(userDTO.getEmail())
                .orElseGet(() -> User.builder()
                        .email(userDTO.getEmail())
                        .role(userDTO.getRole())
                        .build());

        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setPhoneNumber(userDTO.getPhoneNumber());
        user.setAddress(userDTO.getAddress());
        user.setCity(userDTO.getCity());
        user.setState(userDTO.getState());
        user.setZipCode(userDTO.getZipCode());
        user.setIsActive(userDTO.getIsActive() != null ? userDTO.getIsActive() : true);
        
        User savedUser = userRepository.save(user);

        // Create Customer or Technician record if it doesn't exist
        if (savedUser.getRole() == User.Role.CUSTOMER) {
            // Check if customer record already exists for this user
            if (!customerRepository.existsByUser(savedUser)) {
                log.info("Creating Customer record for user: {}", savedUser.getEmail());
                Customer customer = Customer.builder()
                        .user(savedUser)
                        .preferredAddress(savedUser.getAddress())
                        .preferredCity(savedUser.getCity())
                        .preferredPincode(savedUser.getZipCode())
                        .build();
                customerRepository.save(customer);
                log.info("Customer record created successfully");
            } else {
                log.info("Customer record already exists for user: {}", savedUser.getEmail());
            }
        } else if (savedUser.getRole() == User.Role.TECHNICIAN) {
            // Check if technician record already exists for this user
            if (!technicianRepository.existsByUser(savedUser)) {
                log.info("Creating Technician record for user: {}", savedUser.getEmail());
                Technician technician = Technician.builder()
                        .user(savedUser)
                        .skillSet("General") // Default skill set
                        .isAvailable(true)
                        .isVerified(false)
                        .rating(java.math.BigDecimal.ZERO)
                        .experience(0)
                        .build();
                technicianRepository.save(technician);
                log.info("Technician record created successfully");
            } else {
                log.info("Technician record already exists for user: {}", savedUser.getEmail());
            }
        }

        return convertToDTO(savedUser);
    }

    public List<UserDTO> getUsersByRole(User.Role role) {
        List<User> users = userRepository.findByRole(role);
        return users.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public List<UserDTO> getActiveTechnicians() {
        List<User> technicians = userRepository.findByRoleAndIsActive(User.Role.TECHNICIAN, true);
        return technicians.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public org.springframework.data.domain.Page<UserDTO> getAllUsers(org.springframework.data.domain.Pageable pageable) {
        return userRepository.findAll(pageable).map(this::convertToDTO);
    }

    @Transactional
    public UserDTO updateUser(Long id, UserDTO userDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setPhoneNumber(userDTO.getPhoneNumber());
        user.setAddress(userDTO.getAddress());
        user.setCity(userDTO.getCity());
        user.setState(userDTO.getState());
        user.setZipCode(userDTO.getZipCode());
        
        user = userRepository.save(user);
        return convertToDTO(user);
    }

    @Transactional
    public UserDTO toggleUserStatus(Long id, Boolean isActive) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setIsActive(isActive);
        user = userRepository.save(user);
        return convertToDTO(user);
    }

    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found");
        }
        userRepository.deleteById(id);
    }

    private UserDTO convertToDTO(User user) {
        return UserDTO.builder()
                .id(user.getUserId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phoneNumber(user.getPhoneNumber())
                .address(user.getAddress())
                .city(user.getCity())
                .state(user.getState())
                .zipCode(user.getZipCode())
                .role(user.getRole())
                .isActive(user.getIsActive())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }

    // ========== TECHNICIAN METHODS ==========

    @Transactional(readOnly = true)
    public TechnicianDTO getTechnicianById(Long technicianId) {
        log.debug("Fetching technician with ID: {}", technicianId);
        Technician technician = technicianRepository.findById(technicianId)
                .orElseThrow(() -> new RuntimeException("Technician not found with ID: " + technicianId));
        return convertToTechnicianDTO(technician);
    }

    @Transactional(readOnly = true)
    public List<TechnicianDTO> getAllTechnicians() {
        log.debug("Fetching all technicians");
        List<Technician> technicians = technicianRepository.findAll();
        return technicians.stream()
                .map(this::convertToTechnicianDTO)
                .collect(Collectors.toList());
    }

    // ========== CUSTOMER METHODS ==========

    @Transactional(readOnly = true)
    public CustomerDTO getCustomerById(Long customerId) {
        log.debug("Fetching customer with ID: {}", customerId);
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found with ID: " + customerId));
        return convertToCustomerDTO(customer);
    }

    @Transactional(readOnly = true)
    public boolean customerExists(Long customerId) {
        log.debug("Checking if customer exists with ID: {}", customerId);
        return customerRepository.existsById(customerId);
    }

    @Transactional(readOnly = true)
    public List<CustomerDTO> getAllCustomers() {
        log.debug("Fetching all customers");
        List<Customer> customers = customerRepository.findAll();
        return customers.stream()
                .map(this::convertToCustomerDTO)
                .collect(Collectors.toList());
    }

    // ========== PRIVATE CONVERSION METHODS ==========

    private CustomerDTO convertToCustomerDTO(Customer customer) {
        return CustomerDTO.builder()
                .customerId(customer.getCustomerId())
                .firstName(customer.getUser().getFirstName())
                .lastName(customer.getUser().getLastName())
                .email(customer.getUser().getEmail())
                .phone(customer.getUser().getPhoneNumber())
                .address(customer.getUser().getAddress())
                .city(customer.getUser().getCity())
                .state(customer.getUser().getState())
                .zipCode(customer.getUser().getZipCode())
                .isActive(customer.getUser().getIsActive())
                .build();
    }

    private TechnicianDTO convertToTechnicianDTO(Technician technician) {
        return TechnicianDTO.builder()
                .technicianId(technician.getTechnicianId())
                .firstName(technician.getUser().getFirstName())
                .lastName(technician.getUser().getLastName())
                .skill(technician.getSkillSet())
                .experience(technician.getExperience())
                .rating(technician.getRating() != null ? technician.getRating().doubleValue() : 0.0)
                .availabilityStatus(technician.getIsAvailable())
                .isVerified(technician.getIsVerified())
                .build();
    }
}
