package com.hsms.execution_service.service;

import java.time.LocalDateTime;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import com.hsms.execution_service.entity.ServiceRecord;
import com.hsms.execution_service.exception.ResourceNotFoundException;
import com.hsms.execution_service.feignclient.AssignmentClient;
import com.hsms.execution_service.feignclient.PaymentClient;
import com.hsms.execution_service.feignclient.ServiceRequestClient;
import com.hsms.execution_service.model.AssignmentResponseDTO;
import com.hsms.execution_service.model.PaymentRequestDTO;
import com.hsms.execution_service.model.PaymentResponseDTO;
import com.hsms.execution_service.model.ServiceRecordDetailResponseDTO;
import com.hsms.execution_service.model.ServiceRecordRequestDTO;
import com.hsms.execution_service.model.ServiceRecordResponseDTO;
import com.hsms.execution_service.repository.ServiceRecordRepository;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class ServiceRecordServiceImpl implements ServiceRecordService {

    private final ServiceRecordRepository repo;
    private final ServiceRequestClient requestClient;
    private final PaymentClient paymentClient;
    private final AssignmentClient assignmentClient;
    private final ModelMapper mapper;

    @Override
    public ServiceRecordResponseDTO start(ServiceRecordRequestDTO dto) {
        ServiceRecordRequestDTO serviceRequest = requestClient.getRequest(dto.getServiceRequestId());
        if (serviceRequest == null) {
            throw new ResourceNotFoundException("Service request not found");
        }

        AssignmentResponseDTO assignment = assignmentClient.getByServiceRequestId(dto.getServiceRequestId());
        if (assignment == null || !"ACCEPTED".equalsIgnoreCase(assignment.getStatus())) {
            throw new IllegalStateException("Only ACCEPTED assignments can be started");
        }

        ServiceRecord record = new ServiceRecord();
        record.setServiceRequestId(dto.getServiceRequestId());
        record.setStartTime(LocalDateTime.now());
        record.setStatus("IN_PROGRESS");

        ServiceRecord saved = repo.save(record);
        return mapper.map(saved, ServiceRecordResponseDTO.class);
    }

    @Override
    public ServiceRecordDetailResponseDTO complete(Long id, ServiceRecordRequestDTO dto) {
        ServiceRecord record = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Service record not found"));

        if (!"IN_PROGRESS".equalsIgnoreCase(record.getStatus())) {
            throw new IllegalStateException("Only IN_PROGRESS services can be completed");
        }

        record.setEndTime(LocalDateTime.now());
        record.setRemarks(dto.getRemarks());
        record.setActualCost(dto.getActualCost());
        record.setStatus("COMPLETED");

        ServiceRecord saved = repo.save(record);

        PaymentRequestDTO payment = new PaymentRequestDTO();
        payment.setServiceRequestId(record.getServiceRequestId());
        payment.setAmount(record.getActualCost());
        payment.setPaymentMethod(dto.getPaymentMethod()); // enum directly

        PaymentResponseDTO paymentResponse = paymentClient.createPayment(payment);
        return mapper.map(saved, ServiceRecordDetailResponseDTO.class);
    }

    @Override
    public ServiceRecordDetailResponseDTO get(Long id) {
        ServiceRecord record = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Service record not found"));
        return mapper.map(record, ServiceRecordDetailResponseDTO.class);
    }
}
