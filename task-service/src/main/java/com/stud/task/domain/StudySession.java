package com.stud.task.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@Table(name = "study_sessions")
public class StudySession {

    @Id
    private UUID id;

    @Column(nullable = false)
    private UUID userId;

    @Column(nullable = false)
    private UUID taskId;

    @Column(nullable = false)
    private LocalDate sessionDate;

    @Column(nullable = false)
    private LocalTime plannedStartTime;

    @Column(nullable = false)
    private LocalTime plannedEndTime;

    @Column(nullable = false)
    private Integer plannedMinutes;

    private Integer actualMinutes;

    @Column(nullable = false)
    private Integer sessionOrder;

    @Column(nullable = false, precision = 8, scale = 3)
    private BigDecimal priorityScore;

    @Column(nullable = false)
    private boolean fatigueAdjusted;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SessionStatus status;

    public enum SessionStatus {
        PLANNED,
        COMPLETED,
        SKIPPED
    }

    public StudySession() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public UUID getTaskId() {
        return taskId;
    }

    public void setTaskId(UUID taskId) {
        this.taskId = taskId;
    }

    public LocalDate getSessionDate() {
        return sessionDate;
    }

    public void setSessionDate(LocalDate sessionDate) {
        this.sessionDate = sessionDate;
    }

    public LocalTime getPlannedStartTime() {
        return plannedStartTime;
    }

    public void setPlannedStartTime(LocalTime plannedStartTime) {
        this.plannedStartTime = plannedStartTime;
    }

    public LocalTime getPlannedEndTime() {
        return plannedEndTime;
    }

    public void setPlannedEndTime(LocalTime plannedEndTime) {
        this.plannedEndTime = plannedEndTime;
    }

    public Integer getPlannedMinutes() {
        return plannedMinutes;
    }

    public void setPlannedMinutes(Integer plannedMinutes) {
        this.plannedMinutes = plannedMinutes;
    }

    public Integer getActualMinutes() {
        return actualMinutes;
    }

    public void setActualMinutes(Integer actualMinutes) {
        this.actualMinutes = actualMinutes;
    }

    public Integer getSessionOrder() {
        return sessionOrder;
    }

    public void setSessionOrder(Integer sessionOrder) {
        this.sessionOrder = sessionOrder;
    }

    public BigDecimal getPriorityScore() {
        return priorityScore;
    }

    public void setPriorityScore(BigDecimal priorityScore) {
        this.priorityScore = priorityScore;
    }

    public boolean isFatigueAdjusted() {
        return fatigueAdjusted;
    }

    public void setFatigueAdjusted(boolean fatigueAdjusted) {
        this.fatigueAdjusted = fatigueAdjusted;
    }

    public SessionStatus getStatus() {
        return status;
    }

    public void setStatus(SessionStatus status) {
        this.status = status;
    }
} 
