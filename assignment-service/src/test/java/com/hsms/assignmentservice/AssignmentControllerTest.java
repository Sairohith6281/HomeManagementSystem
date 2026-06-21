package com.hsms.assignmentservice;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.hsms.assignmentservice.controller.AssignmentController;
import com.hsms.model.AssignmentDetailResponseDTO;
import com.hsms.model.AssignmentRequestDTO;
import com.hsms.model.AssignmentResponseDTO;
import com.hsms.service.AssignmentService;

@ExtendWith(MockitoExtension.class)
class AssignmentControllerTest {

	private MockMvc mockMvc;

	private ObjectMapper objectMapper;

	@Mock
	private AssignmentService assignmentService;

	@InjectMocks
	private AssignmentController assignmentController;

	@BeforeEach
	void setUp() {
		mockMvc = MockMvcBuilders.standaloneSetup(assignmentController).build();

		objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
	}

	@Test
	void testAssignTechnician() throws Exception {

		AssignmentRequestDTO request = new AssignmentRequestDTO();
		request.setServiceRequestId(1L);
		request.setTechnicianId(2L);

		when(assignmentService.assignTechnician(any(AssignmentRequestDTO.class)))
				.thenReturn(new AssignmentResponseDTO());

		mockMvc.perform(
				post("/assignments").contentType(APPLICATION_JSON).content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isOk());
	}

	@Test
	void testAcceptJob() throws Exception {

		when(assignmentService.acceptJob(anyLong())).thenReturn(new AssignmentResponseDTO());

		mockMvc.perform(put("/assignments/1/accept")).andExpect(status().isOk());
	}

	@Test
	void testRejectJob() throws Exception {

		when(assignmentService.rejectJob(anyLong())).thenReturn(new AssignmentResponseDTO());

		mockMvc.perform(put("/assignments/1/reject")).andExpect(status().isOk());
	}

	@Test
	void testReassignTechnician() throws Exception {

		when(assignmentService.reassignTechnician(anyLong(), anyLong(), any(LocalDateTime.class)))
				.thenReturn(new AssignmentResponseDTO());

		mockMvc.perform(
				put("/assignments/1/reassign").param("technicianId", "5").param("startTime", "2026-06-20T10:30:00"))
				.andExpect(status().isOk());
	}

	@Test
	void testDeleteAssignment() throws Exception {

		doNothing().when(assignmentService).deleteAssignment(anyLong());

		mockMvc.perform(delete("/assignments/1")).andExpect(status().isNoContent());
	}

	@Test
	void testGetAllAssignments() throws Exception {

		AssignmentDetailResponseDTO dto = new AssignmentDetailResponseDTO();

		when(assignmentService.getAllAssignments()).thenReturn(List.of(dto));

		mockMvc.perform(get("/assignments")).andExpect(status().isOk());
	}
}