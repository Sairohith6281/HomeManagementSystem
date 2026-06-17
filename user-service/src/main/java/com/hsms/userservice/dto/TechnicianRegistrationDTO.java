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

    // additonal
    private String skill;
    private Integer experience;
    private Boolean availabilityStatus; // Online or offilne
}