package com.hsms.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TechnicianDTO {

	private Long technicianId;
	private String skill;
	private Integer experience;
	private Boolean availabilityStatus;
	private Double rating;

	// User details
	private UserDTO user;
}
