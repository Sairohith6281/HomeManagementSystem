package com.hsms.execution_service.service;

import java.time.LocalDateTime;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hsms.execution_service.entity.ServiceRecord;
import com.hsms.execution_service.exception.ResourceNotFoundException;
import com.hsms.execution_service.feignclient.AssignmentClient;
import com.hsms.execution_service.feignclient.PaymentClient;
import com.hsms.execution_service.feignclient.ServiceRequestClient;
import com.hsms.execution_service.model.AssignmentResponseDTO;
import com.hsms.execution_service.model.PaymentRequestDTO;
import com.hsms.execution_service.model.ServiceRecordDetailResponseDTO;
import com.hsms.execution_service.model.ServiceRecordRequestDTO;
import com.hsms.execution_service.model.ServiceRecordResponseDTO;
import com.hsms.execution_service.repository.ServiceRecordRepository;


@Service
public class ServiceRecordServiceImpl implements ServiceRecordService {
    @Autowired private ServiceRecordRepository repo;
    @Autowired private ServiceRequestClient requestClient;
    @Autowired private PaymentClient paymentClient;
    @Autowired private AssignmentClient assignmentClient;
    @Autowired private ModelMapper mapper;

    @Override
    public ServiceRecordResponseDTO start(ServiceRecordRequestDTO dto) {
//        requestClient.getRequest(dto.getServiceRequestId()); // validate
    	AssignmentResponseDTO assignment = assignmentClient.getByServiceRequestId(dto.getServiceRequestId());
    	 if (!"ACCEPTED".equalsIgnoreCase(assignment.getStatus())) {
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
//      payment.setMethod(PaymentMethod.valueOf(dto.getPaymentMethod().toUpperCase()));
      paymentClient.createPayment(payment);

        return mapper.map(saved, ServiceRecordDetailResponseDTO.class);
    }

    @Override
    public ServiceRecordDetailResponseDTO get(Long id) {
        return mapper.map(repo.findById(id).orElseThrow(), ServiceRecordDetailResponseDTO.class);
    }
}
