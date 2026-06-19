package com.hsms.analytics_service.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TechnicianDetailResponseDTO {
	private Long technicianId;
	private String technicianName;
	private Long completedJobs;
	private Double rating;
}
