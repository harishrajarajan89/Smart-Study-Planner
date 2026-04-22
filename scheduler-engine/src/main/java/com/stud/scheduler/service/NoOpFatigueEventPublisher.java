package com.stud.scheduler.service;

import com.stud.scheduler.dto.PlannerDtos;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@ConditionalOnProperty(name = "app.fatigue.publisher", havingValue = "none", matchIfMissing = true)
public class NoOpFatigueEventPublisher implements FatigueEventPublisher {

    @Override
    public void publish(UUID userId, PlannerDtos.FatigueUpdateEvent event) {
        // Local fallback mode intentionally skips broker publication.
    }
}
