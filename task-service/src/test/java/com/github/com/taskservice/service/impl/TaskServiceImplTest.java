package com.github.com.taskservice.service.impl;

import com.github.com.taskservice.client.CategoryClient;
import com.github.com.taskservice.entity.Task;
import com.github.com.taskservice.exception.TaskNotFoundException;
import com.github.com.taskservice.mapper.TaskMapper;
import com.github.com.taskservice.repository.TaskRepository;
import com.github.com.taskservice.request.TaskRequest;
import com.github.com.taskservice.response.CategoryDTO;
import com.github.com.taskservice.response.TaskResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private TaskMapper taskMapper;

    @Mock
    private CategoryClient categoryClient;

    @InjectMocks
    private TaskServiceImpl taskServiceImpl;

    private Task task;

    private TaskResponse inputTaskResponse;

    private TaskRequest inputTaskRequest;

    private CategoryDTO categoryDTO;

    @BeforeEach
    public void setUp() {
        task = Task.builder()
                .id(1L)
                .title("Test  task")
                .completed(false)
                .categoryId(1L)
                .build();

        categoryDTO = CategoryDTO.builder()
                .id(1L)
                .name("Test Category")
                .description("Test Description")
                .active(true)
                .build();

        inputTaskResponse = TaskResponse.builder()
                .id(1L)
                .title("Test task")
                .completed(false)
                .category(categoryDTO)
                .build();

        inputTaskRequest = TaskRequest.builder()
                .title("Test task")
                .completed(false)
                .categoryId(1L)
                .build();
    }

    @Test
    @DisplayName("[TaskService] - create() method")
    public void testCreate() {
        when(taskMapper.toEntity(inputTaskRequest)).thenReturn(task);
        when(taskRepository.save(task)).thenReturn(task);
        when(taskMapper.toResponse(task)).thenReturn(inputTaskResponse);

        TaskResponse savedTaskResponse = taskServiceImpl.create(inputTaskRequest);

        assertThat(savedTaskResponse).isNotNull();
        assertThat(savedTaskResponse.getTitle()).isEqualTo(inputTaskRequest.getTitle());
        assertThat(savedTaskResponse.getCompleted()).isEqualTo(inputTaskRequest.getCompleted());
        assertThat(savedTaskResponse.getCategory().getId()).isEqualTo(inputTaskRequest.getCategoryId());
        assertThat(savedTaskResponse.getCategory().getName()).isEqualTo("Test Category");
        assertThat(savedTaskResponse.getCategory().getDescription()).isEqualTo("Test Description");
        assertThat(savedTaskResponse.getCategory().isActive()).isEqualTo(true);

        verify(taskMapper, times(1)).toEntity(inputTaskRequest);
        verify(taskMapper, times(1)).toResponse(task);
        verify(taskRepository, times(1)).save(task);
    }

    @Test
    @DisplayName("[TaskService] - findAll() method")
    public void testFindAll() {
        when(taskRepository.findAll()).thenReturn(List.of(task));
        when(taskMapper.toResponse(task)).thenReturn(inputTaskResponse);
        when(categoryClient.findById(any(String.class))).thenReturn(categoryDTO);

        List<TaskResponse> tasks = taskServiceImpl.findAll();

        assertThat(tasks).isNotNull();
        assertThat(tasks).contains(inputTaskResponse);
        assertThat(tasks.size()).isEqualTo(1);
        assertThat(tasks.get(0).getTitle()).isEqualTo(inputTaskRequest.getTitle());
        assertThat(tasks.get(0).getCompleted()).isEqualTo(inputTaskRequest.getCompleted());
        assertThat(tasks.get(0).getCategory().getId()).isEqualTo(inputTaskRequest.getCategoryId());
        assertThat(tasks.get(0).getCategory().getName()).isEqualTo(categoryDTO.getName());
        assertThat(tasks.get(0).getCategory().getDescription()).isEqualTo(categoryDTO.getDescription());
        assertThat(tasks.get(0).getCategory().isActive()).isEqualTo(categoryDTO.isActive());

        verify(categoryClient, times(1)).findById(any(String.class));
        verify(taskMapper, times(1)).toResponse(task);
        verify(taskRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("[TaskService] - findById() method")
    public void testFindById() {
        when(taskRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(task));
        when(taskMapper.toResponse(task)).thenReturn(inputTaskResponse);
        when(categoryClient.findById(any(String.class))).thenReturn(categoryDTO);

        TaskResponse taskResponse = taskServiceImpl.findById(1L);

        assertThat(taskResponse).isNotNull();
        assertThat(taskResponse.getTitle()).isEqualTo(inputTaskRequest.getTitle());
        assertThat(taskResponse.getCompleted()).isEqualTo(inputTaskRequest.getCompleted());
        assertThat(taskResponse.getCategory().getId()).isEqualTo(inputTaskRequest.getCategoryId());
        assertThat(taskResponse.getCategory().getName()).isEqualTo(categoryDTO.getName());
        assertThat(taskResponse.getCategory().getDescription()).isEqualTo(categoryDTO.getDescription());
        assertThat(taskResponse.getCategory().isActive()).isEqualTo(categoryDTO.isActive());

        verify(categoryClient, times(1)).findById(any(String.class));
        verify(taskRepository, times(1)).findById(1L);
        verify(taskMapper, times(1)).toResponse(task);
    }

    @Test
    @DisplayName("[TaskService] - findById() method when task not found")
    public void testFindByIdWhenTaskNotFound() {
        when(taskRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class, () -> taskServiceImpl.findById(1L));

        verify(taskMapper, never()).toResponse(any(Task.class));
        verify(categoryClient, never()).findById(any(String.class));
    }

    @Test
    @DisplayName("[TaskService] - update() method")
    public void testUpdateTask() {
        TaskResponse updatedTaskResponse = TaskResponse.builder()
                .id(1L)
                .title("updated task")
                .completed(false)
                .category(null)
                .build();

        TaskRequest updatedTaskRequest = TaskRequest.builder()
                .title("updated task")
                .completed(false)
                .categoryId(1L)
                .build();

        when(taskRepository.findById(any(Long.class))).thenReturn(Optional.of(task));
        when(taskRepository.save(task)).thenReturn(task);
        when(taskMapper.toResponse(task)).thenReturn(updatedTaskResponse);

        TaskResponse result = taskServiceImpl.update(any(Long.class), updatedTaskRequest);

        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo(updatedTaskRequest.getTitle());
        assertThat(result.getCompleted()).isEqualTo(updatedTaskRequest.getCompleted());
        assertThat(result.getCategory()).isNull();

        verify(taskRepository, times(1)).findById(any(Long.class));
        verify(taskRepository, times(1)).save(task);
        verify(taskMapper, times(1)).toResponse(task);
    }

    @Test
    @DisplayName("[TaskService] - update() method when task not found")
    public void testUpdateWhenTaskNotFound() {
        when(taskRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class, () -> taskServiceImpl.update(1L, any(TaskRequest.class)));

        verify(taskRepository, times(1)).findById(any(Long.class));
        verify(taskRepository, never()).save(any(Task.class));
        verify(taskMapper, never()).toResponse(any(Task.class));
    }

    @Test
    @DisplayName("[TaskService] - delete() method")
    public void testDelete() {
        taskServiceImpl.delete(any(Long.class));

        verify(taskRepository, times(1)).deleteById(any(Long.class));
    }
}