package com.hsms.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor

public class CustomerDTO {

	private Long customerId;
	private String address;
	private String city;
	private String pincode;

	// User details
	private UserDTO user;
}
