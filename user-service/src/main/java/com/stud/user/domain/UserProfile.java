package com.stud.user.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@Table(name = "user_profiles")
public class UserProfile {

    @Id
    private UUID id;

    @Column(nullable = false)
    private String displayName;

    @Column(nullable = false)
    private String timezone;

    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal dailyStudyHours;

    @Column(nullable = false)
    private Integer baselineEnergyLevel;

    @Column(nullable = false)
    private LocalTime preferredStartTime;

    @Column(nullable = false)
    private LocalTime preferredEndTime;

    public UserProfile() {
    }

    public UserProfile(UUID id,
                       String displayName,
                       String timezone,
                       BigDecimal dailyStudyHours,
                       Integer baselineEnergyLevel,
                       LocalTime preferredStartTime,
                       LocalTime preferredEndTime) {
        this.id = id;
        this.displayName = displayName;
        this.timezone = timezone;
        this.dailyStudyHours = dailyStudyHours;
        this.baselineEnergyLevel = baselineEnergyLevel;
        this.preferredStartTime = preferredStartTime;
        this.preferredEndTime = preferredEndTime;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public BigDecimal getDailyStudyHours() {
        return dailyStudyHours;
    }

    public void setDailyStudyHours(BigDecimal dailyStudyHours) {
        this.dailyStudyHours = dailyStudyHours;
    }

    public Integer getBaselineEnergyLevel() {
        return baselineEnergyLevel;
    }

    public void setBaselineEnergyLevel(Integer baselineEnergyLevel) {
        this.baselineEnergyLevel = baselineEnergyLevel;
    }

    public LocalTime getPreferredStartTime() {
        return preferredStartTime;
    }

    public void setPreferredStartTime(LocalTime preferredStartTime) {
        this.preferredStartTime = preferredStartTime;
    }

    public LocalTime getPreferredEndTime() {
        return preferredEndTime;
    }

    public void setPreferredEndTime(LocalTime preferredEndTime) {
        this.preferredEndTime = preferredEndTime;
    }
}
