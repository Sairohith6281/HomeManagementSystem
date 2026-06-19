package com.hsms.userservice.entity;

import com.hsms.userservice.enums.AvailabilityStatus;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="technicians")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Technician {

    @Id
//    private Long technicianId;

    private Long userId;

    private String skill;

    private Integer experience;

    @Enumerated(EnumType.STRING)
    private AvailabilityStatus availabilityStatus;

    private Double rating;
}