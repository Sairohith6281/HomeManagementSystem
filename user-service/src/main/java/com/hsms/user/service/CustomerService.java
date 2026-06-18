package com.hsms.user.service;

import com.hsms.user.dto.CustomerDTO;
import com.hsms.user.entity.Customer;
import com.hsms.user.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerService {

    private final CustomerRepository customerRepository;

    public List<CustomerDTO> getAllCustomers() {
        List<Customer> customers = customerRepository.findAll();
        return customers.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public CustomerDTO getCustomerById(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        return convertToDTO(customer);
    }

    @Transactional
    public CustomerDTO createCustomer(CustomerDTO customerDTO) {
        Customer customer = Customer.builder()
                .preferredAddress(customerDTO.getAddress())
                .preferredCity(customerDTO.getCity())
                .preferredPincode(customerDTO.getZipCode())
                .build();
        
        customer = customerRepository.save(customer);
        return convertToDTO(customer);
    }

    @Transactional
    public CustomerDTO updateCustomer(Long id, CustomerDTO customerDTO) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        
        if (customerDTO.getAddress() != null) {
            customer.setPreferredAddress(customerDTO.getAddress());
        }
        if (customerDTO.getCity() != null) {
            customer.setPreferredCity(customerDTO.getCity());
        }
        if (customerDTO.getZipCode() != null) {
            customer.setPreferredPincode(customerDTO.getZipCode());
        }
        
        customer = customerRepository.save(customer);
        return convertToDTO(customer);
    }

    @Transactional
    public void deleteCustomer(Long id) {
        if (!customerRepository.existsById(id)) {
            throw new RuntimeException("Customer not found");
        }
        customerRepository.deleteById(id);
    }

    private CustomerDTO convertToDTO(Customer customer) {
        return CustomerDTO.builder()
                .customerId(customer.getCustomerId())
                .firstName(customer.getUser() != null ? customer.getUser().getFirstName() : "")
                .lastName(customer.getUser() != null ? customer.getUser().getLastName() : "")
                .email(customer.getUser() != null ? customer.getUser().getEmail() : "")
                .phone(customer.getUser() != null ? customer.getUser().getPhoneNumber() : "")
                .address(customer.getPreferredAddress())
                .city(customer.getPreferredCity())
                .zipCode(customer.getPreferredPincode())
                .isActive(customer.getUser() != null ? customer.getUser().getIsActive() : false)
                .build();
    }
}
