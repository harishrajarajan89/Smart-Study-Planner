package com.stud.scheduler.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class RootController {

    @GetMapping("/")
    public ResponseEntity<Map<String, Object>> root() {
        return ResponseEntity.ok(Map.of(
                "service", "Scheduler Engine",
                "version", "0.0.1-SNAPSHOT",
                "status", "UP",
                "endpoints", Map.of(
                        "health", "GET /actuator/health",
                        "today_plan", "GET /api/plans/today?userId={UUID}&date={YYYY-MM-DD}",
                        "recalculate", "POST /api/plans/recalculate?userId={UUID}&date={YYYY-MM-DD}",
                        "fatigue", "POST /api/plans/fatigue?userId={UUID}"
                ),
                "description", "Service for calculating daily study plans based on user energy profiles and task requirements"
        ));
    }
}
