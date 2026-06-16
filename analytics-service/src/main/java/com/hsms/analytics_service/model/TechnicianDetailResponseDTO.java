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
    private Long userId;
    private String name;
    private String email;
    private String skill;
    private Integer experience;
    private Boolean availability;
    private Double rating;
}
