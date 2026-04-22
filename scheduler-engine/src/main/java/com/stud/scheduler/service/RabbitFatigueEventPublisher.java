package com.stud.scheduler.service;

import com.stud.scheduler.config.RabbitTopologyConfig;
import com.stud.scheduler.dto.PlannerDtos;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@ConditionalOnProperty(name = "app.fatigue.publisher", havingValue = "rabbit")
public class RabbitFatigueEventPublisher implements FatigueEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public RabbitFatigueEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void publish(UUID userId, PlannerDtos.FatigueUpdateEvent event) {
        rabbitTemplate.convertAndSend(
                RabbitTopologyConfig.STUDY_EVENTS_EXCHANGE,
                RabbitTopologyConfig.FATIGUE_ROUTING_KEY,
                event
        );
    }
}
