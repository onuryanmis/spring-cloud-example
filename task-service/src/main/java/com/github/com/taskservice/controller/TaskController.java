package com.github.com.taskservice.controller;

import com.github.com.taskservice.request.TaskRequest;
import com.github.com.taskservice.response.TaskResponse;
import com.github.com.taskservice.service.TaskService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/task")
@AllArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @GetMapping("/")
    public ResponseEntity<List<TaskResponse>> index() {
        return ResponseEntity.ok(taskService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> show(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.findById(id));
    }

    @PostMapping("/")
    public ResponseEntity<TaskResponse> store(@Validated @RequestBody TaskRequest taskRequest) {
        return ResponseEntity.ok(taskService.create(taskRequest));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponse> update(@PathVariable Long id, @Validated @RequestBody TaskRequest taskRequest) {
        return ResponseEntity.ok(taskService.update(id, taskRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> destroy(@PathVariable Long id) {
        taskService.delete(id);

        return ResponseEntity.ok().build();
    }
}
