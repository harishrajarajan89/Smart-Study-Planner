package com.stud.task.controller;

import com.stud.task.dto.TaskDtos;
import com.stud.task.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public List<TaskDtos.TaskResponse> getTasks(@RequestParam("userId") UUID userId,
                                                @RequestParam(value = "status", required = false) String status) {
        List<TaskDtos.TaskResponse> tasks = taskService.getTasks(userId, status);
        return tasks.isEmpty() ? taskService.createSampleTasks(userId) : tasks;
    }

    @GetMapping("/{taskId}")
    public TaskDtos.TaskResponse getTask(@PathVariable("taskId") UUID taskId) {
        return taskService.getTask(taskId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TaskDtos.TaskResponse createTask(@RequestBody TaskDtos.CreateTaskRequest request) {
        return taskService.createTask(request);
    }

    @PutMapping("/{taskId}")
    public TaskDtos.TaskResponse updateTask(@PathVariable("taskId") UUID taskId,
                                            @RequestBody TaskDtos.UpdateTaskRequest request) {
        return taskService.updateTask(taskId, request);
    }

    @PatchMapping("/{taskId}/progress")
    public TaskDtos.TaskResponse updateProgress(@PathVariable("taskId") UUID taskId,
                                                @RequestBody TaskDtos.TaskProgressUpdateRequest request) {
        return taskService.updateTaskProgress(taskId, request);
    }
}
