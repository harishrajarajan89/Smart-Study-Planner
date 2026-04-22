package com.stud.user.service;

import com.stud.user.domain.UserProfile;
import com.stud.user.dto.EnergyProfileDtos;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UserProfileService {

    private final Map<UUID, UserProfile> profiles = new ConcurrentHashMap<>();

    public EnergyProfileDtos.EnergyProfileResponse getProfile(UUID userId) {
        return toResponse(profiles.computeIfAbsent(userId, this::createDefaultProfile));
    }

    public EnergyProfileDtos.EnergyProfileResponse updateProfile(UUID userId,
                                                                 EnergyProfileDtos.UpdateEnergyProfileRequest request) {
        UserProfile profile = profiles.computeIfAbsent(userId, this::createDefaultProfile);
        profile.setDisplayName(request.displayName() != null ? request.displayName() : profile.getDisplayName());
        profile.setTimezone(request.timezone() != null ? request.timezone() : profile.getTimezone());
        profile.setDailyStudyHours(request.dailyStudyHours() != null ? request.dailyStudyHours() : profile.getDailyStudyHours());
        profile.setBaselineEnergyLevel(request.baselineEnergyLevel() != null
                ? request.baselineEnergyLevel()
                : profile.getBaselineEnergyLevel());
        profile.setPreferredStartTime(request.preferredStartTime() != null
                ? request.preferredStartTime()
                : profile.getPreferredStartTime());
        profile.setPreferredEndTime(request.preferredEndTime() != null
                ? request.preferredEndTime()
                : profile.getPreferredEndTime());
        return toResponse(profile);
    }

    private UserProfile createDefaultProfile(UUID userId) {
        return new UserProfile(
                userId,
                "Learner",
                "Asia/Karachi",
                BigDecimal.valueOf(4.0),
                3,
                LocalTime.of(18, 0),
                LocalTime.of(22, 0)
        );
    }

    private EnergyProfileDtos.EnergyProfileResponse toResponse(UserProfile profile) {
        return new EnergyProfileDtos.EnergyProfileResponse(
                profile.getId(),
                profile.getDisplayName(),
                profile.getTimezone(),
                profile.getDailyStudyHours(),
                profile.getBaselineEnergyLevel(),
                profile.getPreferredStartTime(),
                profile.getPreferredEndTime()
        );
    }
}
