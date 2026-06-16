package com.hsms.userservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "TECHNICIANS")
public class Technician{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long technicianId;  			//PK

	private String skill;

	private Integer experience;

	private Boolean availabilityStatus;

	private Double rating;

	@OneToOne
	@JoinColumn(name = "user_id")
	private User user;  				//FK
}
