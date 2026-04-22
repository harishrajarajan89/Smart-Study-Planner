package com.stud.scheduler.service;

import java.util.UUID;

public interface FatigueStateStore {

    boolean isFatigued(UUID userId);

    void setFatigued(UUID userId, boolean tired);
}
