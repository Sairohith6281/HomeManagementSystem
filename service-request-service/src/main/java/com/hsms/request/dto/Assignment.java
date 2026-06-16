package com.hsms.request.dto;

import java.time.LocalDateTime;

import com.hsms.request.entity.ServiceRequest;
import com.hsms.request.enums.AssignmentStatus;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

public class Assignment {

    @Column(name = "assignment_id")
    private Long assignmentId;

    private ServiceRequest serviceRequest;

    private Technician technician;

    private LocalDateTime assignedDate = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    private AssignmentStatus status = AssignmentStatus.PENDING;


    private LocalDateTime acceptedDate;


    private String rejectionReason;


    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
