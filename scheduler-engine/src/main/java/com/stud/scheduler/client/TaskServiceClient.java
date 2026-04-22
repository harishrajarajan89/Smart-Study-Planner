package com.stud.scheduler.client;

import com.stud.scheduler.dto.PlannerDtos;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "task-service", url = "${TASK_SERVICE_URL:http://localhost:8082}", path = "/api/tasks")
public interface TaskServiceClient {

    @GetMapping
    List<PlannerDtos.TaskSummary> getTasks(@RequestParam("userId") UUID userId,
                                           @RequestParam(value = "status", required = false) String status);
}
