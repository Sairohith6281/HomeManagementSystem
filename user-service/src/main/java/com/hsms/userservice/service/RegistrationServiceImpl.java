package com.hsms.userservice.service;

import org.springframework.stereotype.Service;

import com.hsms.userservice.dto.CustomerRegistrationDTO;
import com.hsms.userservice.dto.TechnicianRegistrationDTO;
import com.hsms.userservice.dto.UserRegistrationDTO;
import com.hsms.userservice.entity.Customer;
import com.hsms.userservice.entity.Technician;
import com.hsms.userservice.entity.User;
import com.hsms.userservice.enums.Role;
import com.hsms.userservice.exception.EmailExistException;
import com.hsms.userservice.repository.CustomerRepository;
import com.hsms.userservice.repository.TechnicianRepository;
import com.hsms.userservice.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RegistrationServiceImpl implements RegistrationService {

	private final UserRepository userRepo;
	private final CustomerRepository customerRepository;
	private final TechnicianRepository technicianRepository;

	@Override
	public String registerUser(UserRegistrationDTO dto) {

		if (userRepo.existsByEmail(dto.getEmail())) {
			throw new EmailExistException("Email already exists");
		}

		// Using Manual Conversion instead of ModelMapper
		User user = new User();

		user.setName(dto.getName());
		user.setEmail(dto.getEmail());
		user.setPassword(dto.getPassword()); // encode later
		user.setRole(dto.getRole());

		userRepo.save(user);

		return "User Registered Successfully";
	}

	@Override
	public String registerCustomer(CustomerRegistrationDTO dto) {

		if (userRepo.existsByEmail(dto.getEmail())) {
			throw new EmailExistException("Email already exists");
		}

		// Using Manual Conversion instead of ModelMapper
		User user = new User();
		user.setName(dto.getName());
		user.setEmail(dto.getEmail());
		user.setPassword(dto.getPassword());
		user.setRole(Role.CUSTOMER);

		User savedUser = userRepo.save(user);

		Customer customer = new Customer();
		customer.setAddress(dto.getAddress());
		customer.setCity(dto.getCity());
		customer.setPincode(dto.getPincode());
		customer.setUser(savedUser);

		customerRepository.save(customer);

		return "Customer Registered Successfully";
	}

	@Override
	public String registerTechnician(TechnicianRegistrationDTO dto) {

		if (userRepo.existsByEmail(dto.getEmail())) {
			throw new EmailExistException("Email already exists");
		}

		// Using Manual Conversion instead of ModelMapper
		User user = new User();

		user.setName(dto.getName());
		user.setEmail(dto.getEmail());
		user.setPassword(dto.getPassword());
		user.setRole(Role.TECHNICIAN);

		User savedUser = userRepo.save(user);

		Technician technician = new Technician();

		technician.setSkill(dto.getSkill());
		technician.setExperience(dto.getExperience());
		technician.setAvailabilityStatus(true);
		technician.setRating(0.0);

		technician.setUser(savedUser);

		technicianRepository.save(technician);

		return "Technician Registered Successfully";
	}

}
