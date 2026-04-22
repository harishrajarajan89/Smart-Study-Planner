package com.stud.scheduler.controller;

import com.stud.scheduler.dto.PlannerDtos;
import com.stud.scheduler.service.DailyPlanService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequestMapping("/api/plans")
public class DailyPlanController {

    private final DailyPlanService dailyPlanService;

    public DailyPlanController(DailyPlanService dailyPlanService) {
        this.dailyPlanService = dailyPlanService;
    }

    @GetMapping("/today")
    public PlannerDtos.DailyPlanResponse getTodayPlan(@RequestParam("userId") UUID userId,
                                                      @RequestParam(value = "date", required = false) LocalDate date) {
        return dailyPlanService.calculateDailyPlan(userId, date != null ? date : LocalDate.now());
    }

    @PostMapping("/recalculate")
    public PlannerDtos.DailyPlanResponse recalculate(@RequestParam("userId") UUID userId,
                                                     @RequestParam(value = "date", required = false) LocalDate date) {
        return dailyPlanService.calculateDailyPlan(userId, date != null ? date : LocalDate.now());
    }

    @PostMapping("/fatigue")
    public PlannerDtos.DailyPlanResponse updateFatigue(@RequestParam("userId") UUID userId,
                                                       @RequestBody PlannerDtos.FatigueCommand command) {
        dailyPlanService.publishFatigueUpdate(userId, command);
        return dailyPlanService.calculateDailyPlan(userId, LocalDate.now());
    }
}
