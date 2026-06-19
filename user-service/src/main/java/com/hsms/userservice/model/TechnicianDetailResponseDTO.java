package com.hsms.userservice.model;

import com.hsms.userservice.enums.AvailabilityStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TechnicianDetailResponseDTO {

    private Long userId;
    private String name;
    private String email;

    private String skill;
    private Integer experience;
    private AvailabilityStatus availabilityStatus;
    private Double rating;
}
