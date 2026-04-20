package com.stud.task.service;

import com.stud.task.domain.Task;
import com.stud.task.dto.TaskDtos;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TaskService {

    private final Map<UUID, Task> tasks = new ConcurrentHashMap<>();

    public List<TaskDtos.TaskResponse> getTasks(UUID userId, String status) {
        return tasks.values().stream()
                .filter(task -> task.getUserId().equals(userId))
                .filter(task -> status == null
                        || "ACTIVE".equalsIgnoreCase(status) && task.getStatus() != Task.TaskStatus.DONE
                        || task.getStatus().name().equalsIgnoreCase(status))
                .sorted(Comparator.comparing(Task::getDeadline))
                .map(this::toResponse)
                .toList();
    }

    public TaskDtos.TaskResponse getTask(UUID taskId) {
        Task task = tasks.get(taskId);
        if (task == null) {
            throw new IllegalArgumentException("Task not found: " + taskId);
        }
        return toResponse(task);
    }

    public TaskDtos.TaskResponse createTask(TaskDtos.CreateTaskRequest request) {
        Task task = new Task();
        task.setId(UUID.randomUUID());
        task.setUserId(request.userId());
        task.setSubject(request.subject());
        task.setTitle(request.title());
        task.setDescription(request.description());
        task.setDeadline(request.deadline() != null ? request.deadline() : LocalDate.now().plusDays(7));
        task.setDifficulty(request.difficulty() != null ? request.difficulty() : 3);
        task.setWeight(request.weight() != null ? request.weight() : BigDecimal.ONE);
        task.setEffortScore(request.effortScore() != null ? request.effortScore() : BigDecimal.valueOf(5.0));
        task.setEstimatedHours(request.estimatedHours() != null ? request.estimatedHours() : BigDecimal.valueOf(4.0));
        task.setRemainingHours(request.remainingHours() != null ? request.remainingHours() : task.getEstimatedHours());
        task.setProgressPercent(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP));
        task.setStatus(Task.TaskStatus.PENDING);
        tasks.put(task.getId(), task);
        return toResponse(task);
    }

    public TaskDtos.TaskResponse updateTask(UUID taskId, TaskDtos.UpdateTaskRequest request) {
        Task task = requireTask(taskId);
        if (request.subject() != null) {
            task.setSubject(request.subject());
        }
        if (request.title() != null) {
            task.setTitle(request.title());
        }
        if (request.description() != null) {
            task.setDescription(request.description());
        }
        if (request.deadline() != null) {
            task.setDeadline(request.deadline());
        }
        if (request.difficulty() != null) {
            task.setDifficulty(request.difficulty());
        }
        if (request.weight() != null) {
            task.setWeight(request.weight());
        }
        if (request.effortScore() != null) {
            task.setEffortScore(request.effortScore());
        }
        if (request.estimatedHours() != null) {
            task.setEstimatedHours(request.estimatedHours());
        }
        if (request.remainingHours() != null) {
            task.setRemainingHours(request.remainingHours());
        }
        if (request.status() != null) {
            task.setStatus(Task.TaskStatus.valueOf(request.status().toUpperCase()));
        }
        return toResponse(task);
    }

    public TaskDtos.TaskResponse updateTaskProgress(UUID taskId, TaskDtos.TaskProgressUpdateRequest request) {
        Task task = requireTask(taskId);
        if (request.progressPercent() != null) {
            task.setProgressPercent(request.progressPercent().setScale(2, RoundingMode.HALF_UP));
        }
        if (request.remainingHours() != null) {
            task.setRemainingHours(request.remainingHours());
        }
        if (task.getProgressPercent().compareTo(BigDecimal.valueOf(100)) >= 0
                || task.getRemainingHours().compareTo(BigDecimal.ZERO) <= 0) {
            task.setProgressPercent(BigDecimal.valueOf(100.00).setScale(2, RoundingMode.HALF_UP));
            task.setRemainingHours(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP));
            task.setStatus(Task.TaskStatus.DONE);
        } else if (task.getProgressPercent().compareTo(BigDecimal.ZERO) > 0) {
            task.setStatus(Task.TaskStatus.IN_PROGRESS);
        }
        return toResponse(task);
    }

    public List<TaskDtos.TaskResponse> createSampleTasks(UUID userId) {
        List<TaskDtos.CreateTaskRequest> defaults = new ArrayList<>();
        defaults.add(new TaskDtos.CreateTaskRequest(userId, "Math", "Practice integration set", "Chapter 5 problem set",
                LocalDate.now().plusDays(2), 5, BigDecimal.valueOf(1.8), BigDecimal.valueOf(8.0),
                BigDecimal.valueOf(3.0), BigDecimal.valueOf(3.0)));
        defaults.add(new TaskDtos.CreateTaskRequest(userId, "History", "Read cold war notes", "Read and summarize",
                LocalDate.now().plusDays(4), 2, BigDecimal.valueOf(1.1), BigDecimal.valueOf(3.5),
                BigDecimal.valueOf(2.0), BigDecimal.valueOf(2.0)));
        defaults.add(new TaskDtos.CreateTaskRequest(userId, "Physics", "Lab revision", "Formula review",
                LocalDate.now().plusDays(1), 4, BigDecimal.valueOf(1.6), BigDecimal.valueOf(6.0),
                BigDecimal.valueOf(2.5), BigDecimal.valueOf(2.5)));
        return defaults.stream().map(this::createTask).toList();
    }

    private Task requireTask(UUID taskId) {
        Task task = tasks.get(taskId);
        if (task == null) {
            throw new IllegalArgumentException("Task not found: " + taskId);
        }
        return task;
    }

    private TaskDtos.TaskResponse toResponse(Task task) {
        return new TaskDtos.TaskResponse(
                task.getId(),
                task.getUserId(),
                task.getSubject(),
                task.getTitle(),
                task.getDescription(),
                task.getDeadline(),
                task.getDifficulty(),
                task.getWeight(),
                task.getEffortScore(),
                task.getEstimatedHours(),
                task.getRemainingHours(),
                task.getProgressPercent(),
                task.getStatus().name()
        );
    }
}
