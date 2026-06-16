package com.hsms.model;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssignmentRequestDTO {

    @NotNull(message = "Technician ID is required")
    private Long technicianId;

    @NotNull(message = "Service Request ID is required")
    private Long serviceRequestId;
}