package com.github.com.taskservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.com.taskservice.request.TaskRequest;
import com.github.com.taskservice.response.CategoryDTO;
import com.github.com.taskservice.response.TaskResponse;
import com.github.com.taskservice.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

@WebMvcTest(TaskController.class)
public class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TaskService taskService;

    private CategoryDTO categoryDTO;

    @BeforeEach
    public void setUp() {
        categoryDTO = CategoryDTO.builder()
                .id(1L)
                .name("Category1")
                .description("Category1 description")
                .build();
    }

    @Test
    @DisplayName("[TaskController] - index() method")
    public void testIndex() throws Exception {
        TaskResponse task1 = new TaskResponse(1L, "Task1", true, categoryDTO);
        TaskResponse task2 = new TaskResponse(2L, "Task2", true, categoryDTO);

        List<TaskResponse> tasks = Arrays.asList(task1, task2);

        when(taskService.findAll()).thenReturn(tasks);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/task/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(tasks)));

        verify(taskService, times(1)).findAll();
        verifyNoMoreInteractions(taskService);
    }

    @Test
    @DisplayName("[TaskController] - show() method")
    public void testShow() throws Exception {
        Long taskId = 1L;
        TaskResponse task = new TaskResponse(taskId, "Task1", true, categoryDTO);

        when(taskService.findById(taskId)).thenReturn(task);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/task/{id}", taskId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(task)));

        verify(taskService, times(1)).findById(taskId);
        verifyNoMoreInteractions(taskService);
    }

    @Test
    @DisplayName("[TaskController] - store() method")
    public void testStore() throws Exception {
        TaskRequest taskRequest = new TaskRequest("Task1", true, 1L);
        TaskResponse createdTask = new TaskResponse(1L, "Task1", true, categoryDTO);

        when(taskService.create(any(TaskRequest.class))).thenReturn(createdTask);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/task/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(createdTask)));

        verify(taskService, times(1)).create(any(TaskRequest.class));
        verifyNoMoreInteractions(taskService);
    }

    @Test
    @DisplayName("[TaskController] - update() method")
    public void testUpdate() throws Exception {
        Long id = 1L;
        TaskRequest taskRequest = new TaskRequest("Task1", true, 1L);
        TaskResponse updatedTask = new TaskResponse(1L, "Task1", true, categoryDTO);

        when(taskService.update(eq(id), any(TaskRequest.class))).thenReturn(updatedTask);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/task/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(updatedTask)));

        verify(taskService, times(1)).update(eq(id), any(TaskRequest.class));
        verifyNoMoreInteractions(taskService);
    }

    @Test
    @DisplayName("[TaskController] - destroy() method")
    public void testDestroy() throws Exception {
        Long id = 1L;

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/task/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());

        verify(taskService, times(1)).delete(id);
        verifyNoMoreInteractions(taskService);
    }
}
