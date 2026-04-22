package com.stud.scheduler.service;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
@ConditionalOnProperty(name = "app.fatigue.store", havingValue = "memory", matchIfMissing = true)
public class InMemoryFatigueStateStore implements FatigueStateStore {

    private final Map<UUID, Boolean> fatigueFlags = new ConcurrentHashMap<>();

    @Override
    public boolean isFatigued(UUID userId) {
        return Boolean.TRUE.equals(fatigueFlags.get(userId));
    }

    @Override
    public void setFatigued(UUID userId, boolean tired) {
        fatigueFlags.put(userId, tired);
    }
}
