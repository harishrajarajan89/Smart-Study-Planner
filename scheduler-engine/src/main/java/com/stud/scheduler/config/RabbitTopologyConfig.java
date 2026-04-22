package com.stud.scheduler.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitTopologyConfig {

    public static final String STUDY_EVENTS_EXCHANGE = "studyplanner.events";
    public static final String FATIGUE_QUEUE = "studyplanner.fatigue.queue";
    public static final String FATIGUE_ROUTING_KEY = "fatigue.updated";

    @Bean
    public DirectExchange studyEventsExchange() {
        return new DirectExchange(STUDY_EVENTS_EXCHANGE);
    }

    @Bean
    public Queue fatigueQueue() {
        return QueueBuilder.durable(FATIGUE_QUEUE).build();
    }

    @Bean
    public Binding fatigueBinding(Queue fatigueQueue, DirectExchange studyEventsExchange) {
        return BindingBuilder.bind(fatigueQueue).to(studyEventsExchange).with(FATIGUE_ROUTING_KEY);
    }

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
