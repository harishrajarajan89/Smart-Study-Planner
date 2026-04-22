package com.stud.scheduler.service;

import com.stud.scheduler.dto.PlannerDtos;

import java.util.UUID;

public interface FatigueEventPublisher {

    void publish(UUID userId, PlannerDtos.FatigueUpdateEvent event);
}
