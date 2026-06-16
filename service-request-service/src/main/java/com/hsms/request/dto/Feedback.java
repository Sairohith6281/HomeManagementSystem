package com.hsms.request.dto;

import java.time.LocalDateTime;

import com.hsms.request.entity.ServiceRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor


public class Feedback {

	private Long feedbackId;

	private ServiceRequest serviceRequest;

	private Customer customer;

	private Technician technician;

	private Integer rating;

	private Integer serviceQualityRating;

	private Integer punctualityRating;

	private Integer behaviorRating;

	private String comments;

	private Boolean wouldRecommend = true;

	private LocalDateTime createdAt;

	private LocalDateTime updatedAt;

}
