package com.hsms.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TechnicianDetailResponseDTO {
	private Long userId;
    private String name;
    private String email;
    private String skill;
    private Integer experience;
    private String availability;
    private Double rating;
    private List<AssignmentResponseDTO> currentAssignments;
}