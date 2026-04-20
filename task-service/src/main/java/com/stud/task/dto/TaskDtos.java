package com.stud.task.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public final class TaskDtos {

    private TaskDtos() {
    }

    public record CreateTaskRequest(
            UUID userId,
            String subject,
            String title,
            String description,
            LocalDate deadline,
            Integer difficulty,
            BigDecimal weight,
            BigDecimal effortScore,
            BigDecimal estimatedHours,
            BigDecimal remainingHours) {
    }

    public record UpdateTaskRequest(
            String subject,
            String title,
            String description,
            LocalDate deadline,
            Integer difficulty,
            BigDecimal weight,
            BigDecimal effortScore,
            BigDecimal estimatedHours,
            BigDecimal remainingHours,
            String status) {
    }

    public record TaskProgressUpdateRequest(
            BigDecimal progressPercent,
            BigDecimal remainingHours) {
    }

    public record TaskResponse(
            UUID id,
            UUID userId,
            String subject,
            String title,
            String description,
            LocalDate deadline,
            Integer difficulty,
            BigDecimal weight,
            BigDecimal effortScore,
            BigDecimal estimatedHours,
            BigDecimal remainingHours,
            BigDecimal progressPercent,
            String status) {
    }
}
