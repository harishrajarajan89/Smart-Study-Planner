package com.stud.scheduler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class SchedulerEngineApplication {

    public static void main(String[] args) {
        SpringApplication.run(SchedulerEngineApplication.class, args);
    }
}
