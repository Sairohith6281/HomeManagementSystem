package com.hsms.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TechnicianDTO {
    private Long technicianId;
    private String firstName;
    private String lastName;
    private String skill;
    private Integer experience;
    private Double rating;
    private Boolean availabilityStatus;
    private Boolean isVerified;
}
