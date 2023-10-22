package com.github.com.taskservice.service;

import com.github.com.taskservice.request.TaskRequest;
import com.github.com.taskservice.response.TaskResponse;

import java.util.List;

public interface TaskService {

    TaskResponse create(TaskRequest taskRequest);

    List<TaskResponse> findAll();

    TaskResponse findById(Long id);

    TaskResponse update(Long id, TaskRequest taskRequest);

    void delete(Long id);
}
