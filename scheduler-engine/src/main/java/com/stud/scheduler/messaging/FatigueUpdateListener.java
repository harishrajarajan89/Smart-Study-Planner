package com.stud.scheduler.messaging;

import com.stud.scheduler.config.RabbitTopologyConfig;
import com.stud.scheduler.dto.PlannerDtos;
import com.stud.scheduler.service.FatigueStateStore;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "app.fatigue.publisher", havingValue = "rabbit")
public class FatigueUpdateListener {

    private final FatigueStateStore fatigueStateStore;

    public FatigueUpdateListener(FatigueStateStore fatigueStateStore) {
        this.fatigueStateStore = fatigueStateStore;
    }

    @RabbitListener(queues = RabbitTopologyConfig.FATIGUE_QUEUE)
    public void handleFatigueUpdate(PlannerDtos.FatigueUpdateEvent event) {
        fatigueStateStore.setFatigued(event.userId(), Boolean.TRUE.equals(event.fatigue().isTired()));
    }
}
