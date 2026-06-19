package com.hsms.userservice.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hsms.userservice.entity.Customer;
import com.hsms.userservice.entity.Technician;
import com.hsms.userservice.exception.EmailExistException;
import com.hsms.userservice.exception.ResourceNotFoundException;
import com.hsms.userservice.feign.AuthFeignClient;
import com.hsms.userservice.model.CustomerDetailResponseDTO;
import com.hsms.userservice.model.CustomerProfileRequestDTO;
import com.hsms.userservice.model.TechnicianDetailResponseDTO;
import com.hsms.userservice.model.TechnicianProfileRequestDTO;
import com.hsms.userservice.model.UserProfileResponseDTO;
import com.hsms.userservice.repository.CustomerRepository;
import com.hsms.userservice.repository.TechnicianRepository;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private TechnicianRepository technicianRepository;

    @Autowired
    private AuthFeignClient authFeignClient;

    @Autowired
    private ModelMapper modelMapper;

//    @Override
//    public CustomerDetailResponseDTO createCustomer(
//            CustomerProfileRequestDTO dto) {
//
//        UserProfileResponseDTO user =
//                authFeignClient.getUserById(dto.getUserId());
//
//        Customer customer =
//                modelMapper.map(dto, Customer.class);
//
//        Customer saved =
//                customerRepository.save(customer);
//
//        CustomerDetailResponseDTO response =
//                modelMapper.map(saved,
//                        CustomerDetailResponseDTO.class);
//
//        response.setUserId(user.getUserId());
//        response.setName(user.getName());
//        response.setEmail(user.getEmail());
//
//        return response;
//    }

    @Override
    public CustomerDetailResponseDTO updateCustomer(
            Long userId,
            CustomerProfileRequestDTO dto) {

        Customer customer =
                customerRepository.findByUserId(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Customer Not Found"));

        customer.setAddress(dto.getAddress());
        customer.setCity(dto.getCity());
        customer.setPincode(dto.getPincode());

        Customer updated =
                customerRepository.save(customer);

        UserProfileResponseDTO user =
                authFeignClient.getUserById(userId);

        CustomerDetailResponseDTO response =
                modelMapper.map(updated,
                        CustomerDetailResponseDTO.class);

        response.setUserId(user.getUserId());
        response.setName(user.getName());
        response.setEmail(user.getEmail());

        return response;
    }

    @Override
    public CustomerDetailResponseDTO getCustomer(
            Long userId) {

        Customer customer =
                customerRepository.findByUserId(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Customer Not Found"));

        UserProfileResponseDTO user =
                authFeignClient.getUserById(userId);

        CustomerDetailResponseDTO response =
                modelMapper.map(customer,
                        CustomerDetailResponseDTO.class);

        response.setUserId(user.getUserId());
        response.setName(user.getName());
        response.setEmail(user.getEmail());

        return response;
    }

    @Override
    public List<CustomerDetailResponseDTO> getAllCustomers() {

        return customerRepository.findAll()
                .stream()
                .map(customer -> {

                    UserProfileResponseDTO user =
                            authFeignClient.getUserById(
                                    customer.getUserId());

                    CustomerDetailResponseDTO dto =
                            modelMapper.map(
                                    customer,
                                    CustomerDetailResponseDTO.class);

                    dto.setUserId(user.getUserId());
                    dto.setName(user.getName());
                    dto.setEmail(user.getEmail());

                    return dto;

                }).collect(Collectors.toList());
    }

    @Override
    public void deleteCustomer(Long userId) {

        Customer customer =
                customerRepository.findByUserId(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Customer Not Found"));

        customerRepository.delete(customer);
    }

    @Override
    public CustomerDetailResponseDTO getCustomerById(
            Long customerId) {

        Customer customer =
                customerRepository.findById(customerId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Customer Not Found"));

        UserProfileResponseDTO user =
                authFeignClient.getUserById(
                        customer.getUserId());

        CustomerDetailResponseDTO response =
                modelMapper.map(customer,
                        CustomerDetailResponseDTO.class);

        response.setUserId(user.getUserId());
        response.setName(user.getName());
        response.setEmail(user.getEmail());

        return response;
    }

    // ================= TECHNICIAN =================

    @Override
    public TechnicianDetailResponseDTO createTechnician(
            TechnicianProfileRequestDTO dto) {

        UserProfileResponseDTO user =
                authFeignClient.getUserById(dto.getUserId());

        Technician technician =
                modelMapper.map(dto,
                        Technician.class);

        technician.setRating(0.0);

        Technician saved =
                technicianRepository.save(technician);

        TechnicianDetailResponseDTO response =
                modelMapper.map(saved,
                        TechnicianDetailResponseDTO.class);

        response.setUserId(user.getUserId());
        response.setName(user.getName());
        response.setEmail(user.getEmail());

        return response;
    }

    @Override
    public TechnicianDetailResponseDTO updateTechnician(
            Long userId,
            TechnicianProfileRequestDTO dto) {

        Technician technician =
                technicianRepository.findByUserId(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Technician Not Found"));

        technician.setSkill(dto.getSkill());
        technician.setExperience(dto.getExperience());
        technician.setAvailabilityStatus(dto.getAvailabilityStatus());

        Technician updated =
                technicianRepository.save(technician);

        UserProfileResponseDTO user =
                authFeignClient.getUserById(userId);

        TechnicianDetailResponseDTO response =
                modelMapper.map(updated,
                        TechnicianDetailResponseDTO.class);

        response.setUserId(user.getUserId());
        response.setName(user.getName());
        response.setEmail(user.getEmail());

        return response;
    }

    @Override
    public TechnicianDetailResponseDTO getTechnician(
            Long userId) {

        Technician technician =
                technicianRepository.findByUserId(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Technician Not Found"));

        UserProfileResponseDTO user =
                authFeignClient.getUserById(userId);

        TechnicianDetailResponseDTO response =
                modelMapper.map(technician,
                        TechnicianDetailResponseDTO.class);

        response.setUserId(user.getUserId());
        response.setName(user.getName());
        response.setEmail(user.getEmail());

        return response;
    }

    @Override
    public List<TechnicianDetailResponseDTO> getAllTechnicians() {

        return technicianRepository.findAll()
                .stream()
                .map(technician -> {

                    UserProfileResponseDTO user =
                            authFeignClient.getUserById(
                                    technician.getUserId());

                    TechnicianDetailResponseDTO dto =
                            modelMapper.map(
                                    technician,
                                    TechnicianDetailResponseDTO.class);

                    dto.setUserId(user.getUserId());
                    dto.setName(user.getName());
                    dto.setEmail(user.getEmail());

                    return dto;

                }).collect(Collectors.toList());
    }

    @Override
    public void deleteTechnician(Long userId) {

        Technician technician =
                technicianRepository.findByUserId(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Technician Not Found"));

        technicianRepository.delete(technician);
    }

    @Override
    public TechnicianDetailResponseDTO getTechnicianById(
            Long technicianId) {

        Technician technician =
                technicianRepository.findById(technicianId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Technician Not Found"));

        UserProfileResponseDTO user =
                authFeignClient.getUserById(
                        technician.getUserId());

        TechnicianDetailResponseDTO response =
                modelMapper.map(technician,
                        TechnicianDetailResponseDTO.class);

        response.setUserId(user.getUserId());
        response.setName(user.getName());
        response.setEmail(user.getEmail());

        return response;
    }
    
    
    //
    @Override
    public CustomerDetailResponseDTO createCustomer(
            CustomerProfileRequestDTO dto,
            Long userId,
            String email) {

        Customer customer =
                modelMapper.map(dto,
                        Customer.class);

        customer.setUserId(userId);

        Customer saved =
                customerRepository.save(customer);

        CustomerDetailResponseDTO response =
                modelMapper.map(saved,
                        CustomerDetailResponseDTO.class);

        response.setUserId(userId);
        response.setEmail(email);

        return response;
    }
}