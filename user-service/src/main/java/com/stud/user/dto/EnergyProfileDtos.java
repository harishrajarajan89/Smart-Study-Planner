package com.stud.user.dto;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.UUID;

public final class EnergyProfileDtos {

    private EnergyProfileDtos() {
    }

    public record EnergyProfileResponse(
            UUID userId,
            String displayName,
            String timezone,
            BigDecimal dailyStudyHours,
            Integer baselineEnergyLevel,
            LocalTime preferredStartTime,
            LocalTime preferredEndTime) {
    }

    public record UpdateEnergyProfileRequest(
            String displayName,
            String timezone,
            BigDecimal dailyStudyHours,
            Integer baselineEnergyLevel,
            LocalTime preferredStartTime,
            LocalTime preferredEndTime) {
    }
}
