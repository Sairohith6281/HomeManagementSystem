package com.hsms.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TechnicianDTO {
	private Long userId;
    private String name;
    private String email;
    private String skill;
    private Integer experience;
    private Boolean availability;
    private Double rating;
}