package com.stud.user.controller;

import com.stud.user.dto.EnergyProfileDtos;
import com.stud.user.service.UserProfileService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserProfileController {

    private final UserProfileService userProfileService;

    public UserProfileController(UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }

    @GetMapping("/{userId}/energy-profile")
    public EnergyProfileDtos.EnergyProfileResponse getEnergyProfile(@PathVariable("userId") UUID userId) {
        return userProfileService.getProfile(userId);
    }

    @PutMapping("/{userId}/energy-profile")
    public EnergyProfileDtos.EnergyProfileResponse updateEnergyProfile(
            @PathVariable("userId") UUID userId,
            @RequestBody EnergyProfileDtos.UpdateEnergyProfileRequest request) {
        return userProfileService.updateProfile(userId, request);
    }
}
