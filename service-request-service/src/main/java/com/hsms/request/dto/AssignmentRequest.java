package com.hsms.request.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class AssignmentRequest {

	@NotNull(message = "Service request ID is required")
	private Long requestId;

	@NotNull(message = "Technician ID is required")
	private Long technicianId;

	private String notes;
}
