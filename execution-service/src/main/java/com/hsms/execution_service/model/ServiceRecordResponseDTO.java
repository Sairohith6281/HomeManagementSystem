package com.hsms.execution_service.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor

public class ServiceRecordResponseDTO {
    private Long recordId;
    private Long serviceRequestId;
    private String status;
} 