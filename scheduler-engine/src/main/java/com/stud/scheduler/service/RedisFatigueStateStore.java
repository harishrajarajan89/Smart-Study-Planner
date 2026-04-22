package com.stud.scheduler.service;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.UUID;

@Component
@ConditionalOnProperty(name = "app.fatigue.store", havingValue = "redis")
public class RedisFatigueStateStore implements FatigueStateStore {

    private final StringRedisTemplate redisTemplate;

    public RedisFatigueStateStore(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public boolean isFatigued(UUID userId) {
        return Boolean.parseBoolean(redisTemplate.opsForValue().get(fatigueKey(userId)));
    }

    @Override
    public void setFatigued(UUID userId, boolean tired) {
        redisTemplate.opsForValue().set(fatigueKey(userId), String.valueOf(tired), Duration.ofHours(12));
    }

    private String fatigueKey(UUID userId) {
        return "fatigue:user:" + userId;
    }
}
