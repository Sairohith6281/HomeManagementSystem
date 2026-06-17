package com.hsms.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerRegistrationDTO {

	private String name;
	private String email;
	private String password;

	//Addtional Data
	private String address;
	private String city;
	private String pincode;
}