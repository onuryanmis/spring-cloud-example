package com.github.com.taskservice.service.impl;

import com.github.com.taskservice.client.CategoryClient;
import com.github.com.taskservice.request.TaskRequest;
import com.github.com.taskservice.response.CategoryDTO;
import com.github.com.taskservice.entity.Task;
import com.github.com.taskservice.exception.TaskNotFoundException;
import com.github.com.taskservice.mapper.TaskMapper;
import com.github.com.taskservice.repository.TaskRepository;
import com.github.com.taskservice.response.TaskResponse;
import com.github.com.taskservice.service.TaskService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final CategoryClient categoryClient;

    @Override
    public TaskResponse create(TaskRequest taskRequest) {
        Task task = taskRepository.save(taskMapper.toEntity(taskRequest));
        return taskMapper.toResponse(task);
    }

    @Override
    public List<TaskResponse> findAll() {
        return taskRepository.findAll().stream()
                .map(task -> {
                    CategoryDTO category = categoryClient.findById(String.valueOf(task.getCategoryId()));
                    TaskResponse taskResponse = taskMapper.toResponse(task);
                    taskResponse.setCategory(category);
                    return taskResponse;
                })
                .collect(Collectors.toList());
    }

    @Override
    public TaskResponse findById(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(TaskNotFoundException::new);

        CategoryDTO category = categoryClient.findById(String.valueOf(task.getCategoryId()));
        TaskResponse taskResponse = taskMapper.toResponse(task);

        taskResponse.setCategory(category);

        return taskResponse;
    }

    @Override
    public TaskResponse update(Long id, TaskRequest taskRequest) {
        Task task = taskRepository.findById(id)
                .orElseThrow(TaskNotFoundException::new);

        task.setTitle(taskRequest.getTitle());
        task.setCompleted(taskRequest.getCompleted());
        task.setCategoryId(taskRequest.getCategoryId());

        task = taskRepository.save(task);

        return taskMapper.toResponse(task);
    }

    @Override
    public void delete(Long id) {
        taskRepository.deleteById(id);
    }
}
