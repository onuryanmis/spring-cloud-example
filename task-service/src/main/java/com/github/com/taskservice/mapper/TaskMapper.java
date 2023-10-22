package com.github.com.taskservice.mapper;

import com.github.com.taskservice.entity.Task;
import com.github.com.taskservice.request.TaskRequest;
import com.github.com.taskservice.response.TaskResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TaskMapper {

    TaskResponse toResponse(Task task);

    Task toEntity(TaskRequest taskRequest);
}
