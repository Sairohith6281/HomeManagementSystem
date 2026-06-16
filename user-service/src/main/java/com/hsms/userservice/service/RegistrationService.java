package com.hsms.userservice.service;

import com.hsms.userservice.dto.CustomerRegistrationDTO;
import com.hsms.userservice.dto.TechnicianRegistrationDTO;
import com.hsms.userservice.dto.UserRegistrationDTO;

public interface RegistrationService {

    String registerUser(UserRegistrationDTO dto);

    String registerCustomer(CustomerRegistrationDTO dto);

    String registerTechnician(TechnicianRegistrationDTO dto);
}