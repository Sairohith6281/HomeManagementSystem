package com.hsms.execution_service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import com.hsms.execution_service.controller.ServiceRecordController;
import com.hsms.execution_service.model.ServiceRecordDetailResponseDTO;
import com.hsms.execution_service.model.ServiceRecordRequestDTO;
import com.hsms.execution_service.model.ServiceRecordResponseDTO;
import com.hsms.execution_service.service.ServiceRecordService;

@ExtendWith(MockitoExtension.class)
class ServiceRecordControllerTest {

    @Mock
    private ServiceRecordService service;

    @InjectMocks
    private ServiceRecordController controller;

    // ---------------- START ----------------
    @Test
    void start_shouldReturnResponse() {

        ServiceRecordRequestDTO request = new ServiceRecordRequestDTO();
        ServiceRecordResponseDTO response = new ServiceRecordResponseDTO();

        when(service.start(request)).thenReturn(response);

        ResponseEntity<ServiceRecordResponseDTO> result = controller.start(request);

        assertNotNull(result);
        assertEquals(200, result.getStatusCode().value());
        assertEquals(response, result.getBody());

        verify(service).start(request);
    }

    // ---------------- COMPLETE ----------------
    @Test
    void complete_shouldReturnResponse() {

        Long id = 1L;
        ServiceRecordRequestDTO request = new ServiceRecordRequestDTO();
        ServiceRecordDetailResponseDTO response = new ServiceRecordDetailResponseDTO();

        when(service.complete(id, request)).thenReturn(response);

        ResponseEntity<ServiceRecordDetailResponseDTO> result =
                controller.complete(id, request);

        assertNotNull(result);
        assertEquals(200, result.getStatusCode().value());
        assertEquals(response, result.getBody());

        verify(service).complete(id, request);
    }

    // ---------------- GET ----------------
    @Test
    void get_shouldReturnResponse() {

        Long id = 1L;
        ServiceRecordDetailResponseDTO response = new ServiceRecordDetailResponseDTO();

        when(service.get(id)).thenReturn(response);

        ResponseEntity<ServiceRecordDetailResponseDTO> result =
                controller.get(id);

        assertNotNull(result);
        assertEquals(200, result.getStatusCode().value());
        assertEquals(response, result.getBody());

        verify(service).get(id);
    }
}