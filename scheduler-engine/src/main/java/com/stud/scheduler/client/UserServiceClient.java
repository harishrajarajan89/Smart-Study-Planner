package com.stud.scheduler.client;

import com.stud.scheduler.dto.PlannerDtos;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "user-service", path = "/api/users")
public interface UserServiceClient {

    @GetMapping("/{userId}/energy-profile")
    PlannerDtos.UserEnergyProfile getEnergyProfile(@PathVariable("userId") UUID userId);
}
