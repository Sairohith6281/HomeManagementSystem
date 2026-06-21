package com.hsms.execution_service.service;

import java.time.LocalDateTime;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import com.hsms.execution_service.entity.ServiceRecord;
import com.hsms.execution_service.exception.ResourceNotFoundException;
import com.hsms.execution_service.feignclient.AssignmentClient;
import com.hsms.execution_service.feignclient.BookingserviceClient;
import com.hsms.execution_service.feignclient.PaymentClient;
import com.hsms.execution_service.model.AssignmentResponseDTO;
import com.hsms.execution_service.model.PaymentRequestDTO;
import com.hsms.execution_service.model.ServiceRecordDetailResponseDTO;
import com.hsms.execution_service.model.ServiceRecordRequestDTO;
import com.hsms.execution_service.model.ServiceRecordResponseDTO;
import com.hsms.execution_service.repository.ServiceRecordRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ServiceRecordServiceImpl implements ServiceRecordService {

    private final ServiceRecordRepository repo;
    private final BookingserviceClient requestClient;
    private final PaymentClient paymentClient;
    private final AssignmentClient assignmentClient;
    private final ModelMapper mapper;

    @Override
    public ServiceRecordResponseDTO start(ServiceRecordRequestDTO dto) {
        // Validate service request
        var serviceRequest = requestClient.getRequest(dto.getServiceRequestId());
        if (serviceRequest == null || !"ASSIGNED".equalsIgnoreCase(serviceRequest.getStatus())) {
            throw new IllegalStateException("Only ASSIGNED requests can be started");
        }

        // Validate assignment
        AssignmentResponseDTO assignment = assignmentClient.getByServiceRequestId(dto.getServiceRequestId());
        if (assignment == null || !"ACCEPTED".equalsIgnoreCase(assignment.getStatus())) {
            throw new IllegalStateException("Only ACCEPTED assignments can be started");
        }

        ServiceRecord serviceRecord = new ServiceRecord();
        serviceRecord.setServiceRequestId(dto.getServiceRequestId());
        serviceRecord.setStartTime(LocalDateTime.now());
        serviceRecord.setStatus("IN_PROGRESS");

        ServiceRecord savedRecord = repo.save(serviceRecord);

        // Update Booking Service status
        requestClient.updateStatus(dto.getServiceRequestId(), "IN_PROGRESS");

        return mapper.map(savedRecord, ServiceRecordResponseDTO.class);
    }

    @Override
    public ServiceRecordDetailResponseDTO complete(Long id, ServiceRecordRequestDTO dto) {
        ServiceRecord serviceRecord = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Service record not found"));

        if (!"IN_PROGRESS".equalsIgnoreCase(serviceRecord.getStatus())) {
            throw new IllegalStateException("Only IN_PROGRESS services can be completed");
        }

        serviceRecord.setEndTime(LocalDateTime.now());
        serviceRecord.setRemarks(dto.getRemarks());
        serviceRecord.setActualCost(dto.getActualCost());
        serviceRecord.setStatus("COMPLETED");

        ServiceRecord savedRecord = repo.save(serviceRecord);

        // Trigger payment (no unused variable)
        PaymentRequestDTO payment = new PaymentRequestDTO();
        payment.setServiceRequestId(serviceRecord.getServiceRequestId());
        payment.setAmount(serviceRecord.getActualCost());
        payment.setPaymentMethod(dto.getPaymentMethod());

        paymentClient.createPayment(payment);

        // Update Booking Service status
        requestClient.updateStatus(serviceRecord.getServiceRequestId(), "COMPLETED");

        return mapper.map(savedRecord, ServiceRecordDetailResponseDTO.class);
    }

    @Override
    public ServiceRecordDetailResponseDTO get(Long id) {
        ServiceRecord serviceRecord = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Service record not found"));
        return mapper.map(serviceRecord, ServiceRecordDetailResponseDTO.class);
    }
}
