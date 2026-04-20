package com.stud.task.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "tasks")
public class Task {

    @Id
    private UUID id;

    @Column(nullable = false)
    private UUID userId;

    @Column(nullable = false)
    private String subject;

    @Column(nullable = false)
    private String title;

    @Column(length = 1000)
    private String description;

    @Column(nullable = false)
    private LocalDate deadline;

    @Column(nullable = false)
    private Integer difficulty;

    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal weight;

    @Column(nullable = false, precision = 6, scale = 2)
    private BigDecimal effortScore;

    @Column(nullable = false, precision = 6, scale = 2)
    private BigDecimal estimatedHours;

    @Column(nullable = false, precision = 6, scale = 2)
    private BigDecimal remainingHours;

    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal progressPercent;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskStatus status;

    public enum TaskStatus {
        PENDING,
        IN_PROGRESS,
        DONE
    }

    public Task() {
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

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }

    public Integer getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Integer difficulty) {
        this.difficulty = difficulty;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public BigDecimal getEffortScore() {
        return effortScore;
    }

    public void setEffortScore(BigDecimal effortScore) {
        this.effortScore = effortScore;
    }

    public BigDecimal getEstimatedHours() {
        return estimatedHours;
    }

    public void setEstimatedHours(BigDecimal estimatedHours) {
        this.estimatedHours = estimatedHours;
    }

    public BigDecimal getRemainingHours() {
        return remainingHours;
    }

    public void setRemainingHours(BigDecimal remainingHours) {
        this.remainingHours = remainingHours;
    }

    public BigDecimal getProgressPercent() {
        return progressPercent;
    }

    public void setProgressPercent(BigDecimal progressPercent) {
        this.progressPercent = progressPercent;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }
}
