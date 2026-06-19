package com.hsms.userservice.model;

import com.hsms.userservice.enums.AvailabilityStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TechnicianProfileRequestDTO {

    private Long userId;
    private String skill;
    private Integer experience;
    private AvailabilityStatus availabilityStatus;
}