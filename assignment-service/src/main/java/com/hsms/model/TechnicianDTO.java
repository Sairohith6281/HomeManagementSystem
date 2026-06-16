package com.hsms.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TechnicianDTO {
    private Long id;
    private String name;
    private String skill;
    private double rating;
}
