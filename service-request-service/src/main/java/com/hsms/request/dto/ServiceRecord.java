package com.hsms.request.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class ServiceRecord {

	private Long serviceId;

	private Long requestId;

	private Long technicianId;

	private String technicianName;

	private LocalDateTime startTime;

	private LocalDateTime endTime;

	private Integer durationMinutes;

	private String remarks;

	private Double actualCost;

	private Double basePrice;

	private String address;

	private String categoryName;

	private String customerName;

	private String customerPhone;

	private Integer serviceImagesCount;

	private LocalDateTime createdAt;

	private LocalDateTime updatedAt;
}
