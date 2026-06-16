package com.hsms.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TechnicianRegistrationDTO {

    private String name;
    private String email;
    private String password;

    private String skill;
    private Integer experience;
    private Boolean availabilityStatus;
}