package com.stud.scheduler.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public final class PlannerDtos {

    private PlannerDtos() {
    }

    public record UserEnergyProfile(
            UUID userId,
            String displayName,
            String timezone,
            BigDecimal dailyStudyHours,
            Integer baselineEnergyLevel,
            LocalTime preferredStartTime,
            LocalTime preferredEndTime) {
    }

    public record TaskSummary(
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

    public record FatiguePayload(
            Boolean isTired,
            Integer reportedEnergyLevel,
            String note) {
    }

    public record EventContext(
            String timezone,
            LocalDate currentPlanDate) {
    }

    public record FatigueUpdateEvent(
            String eventId,
            String eventType,
            OffsetDateTime occurredAt,
            UUID userId,
            String source,
            FatiguePayload fatigue,
            EventContext context) {
    }

    public record FatigueCommand(
            Boolean tired,
            Integer reportedEnergyLevel,
            String note,
            String timezone) {
    }

    public record StudyBlock(
            UUID taskId,
            String subject,
            String title,
            Integer blockMinutes,
            LocalTime plannedStart,
            LocalTime plannedEnd,
            BigDecimal priorityScore,
            boolean fatigueAdjusted) {
    }

    public record DailyPlanResponse(
            UUID userId,
            LocalDate planDate,
            BigDecimal baselineHours,
            BigDecimal effectiveHours,
            boolean fatigueApplied,
            List<StudyBlock> sessions,
            List<String> rationale) {
    }
}
