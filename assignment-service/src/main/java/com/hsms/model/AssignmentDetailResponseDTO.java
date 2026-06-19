package com.hsms.model;

import java.time.LocalDateTime;
import com.hsms.entity.AssignmentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssignmentDetailResponseDTO {
    private Long id;
    private Long technicianId;
    private Long serviceRequestId;
    private LocalDateTime assignedDate;
    private AssignmentStatus status;
    private TechnicianDetailResponseDTO technician; 
}