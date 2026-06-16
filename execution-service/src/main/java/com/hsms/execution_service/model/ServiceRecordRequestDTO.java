package com.hsms.execution_service.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ServiceRecordRequestDTO {
    private Long serviceRequestId;
    private String startTime;
    private String endTime;
    private String remarks;
    private Double actualCost;
}
