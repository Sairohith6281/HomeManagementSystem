package com.hsms.request.dto;

import java.time.LocalDateTime;

public class AssignmentResponse {

    private Long assignmentId;

    private Long requestId;

    private String requestAddress;

    private String requestCity;

    private String requestPincode;

    private LocalDateTime scheduledDateTime;

    private String categoryName;

    private Double basePrice;

    private Long technicianId;

    private String technicianName;

    private String technicianSkill;

    private Integer technicianExperience;

    private Double technicianRating;

    //private AssignmentStatus status;

    private LocalDateTime assignedDate;

    private LocalDateTime acceptedDate;

    private String rejectionReason;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private String customerName;

    private String customerPhone;

    private String customerEmail;
}
