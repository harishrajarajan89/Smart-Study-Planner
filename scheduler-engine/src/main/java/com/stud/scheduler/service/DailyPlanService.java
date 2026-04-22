package com.stud.scheduler.service;

import com.stud.scheduler.client.TaskServiceClient;
import com.stud.scheduler.client.UserServiceClient;
import com.stud.scheduler.dto.PlannerDtos;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
public class DailyPlanService {

    private static final BigDecimal FATIGUE_REDUCTION_FACTOR = BigDecimal.valueOf(0.60);
    private static final BigDecimal HIGH_DIFFICULTY_PENALTY = BigDecimal.valueOf(0.65);
    private static final int DEFAULT_BLOCK_MINUTES = 60;
    private static final int SHORT_BLOCK_MINUTES = 45;
    private static final int SHORT_BREAK_MINUTES = 10;
    private static final int LONG_BREAK_MINUTES = 20;

    private final UserServiceClient userServiceClient;
    private final TaskServiceClient taskServiceClient;
    private final FatigueEventPublisher fatigueEventPublisher;
    private final FatigueStateStore fatigueStateStore;

    public DailyPlanService(UserServiceClient userServiceClient,
                            TaskServiceClient taskServiceClient,
                            FatigueEventPublisher fatigueEventPublisher,
                            FatigueStateStore fatigueStateStore) {
        this.userServiceClient = userServiceClient;
        this.taskServiceClient = taskServiceClient;
        this.fatigueEventPublisher = fatigueEventPublisher;
        this.fatigueStateStore = fatigueStateStore;
    }

    public PlannerDtos.DailyPlanResponse calculateDailyPlan(UUID userId, LocalDate planDate) {
        PlannerDtos.UserEnergyProfile profile = userServiceClient.getEnergyProfile(userId);
        List<PlannerDtos.TaskSummary> tasks = taskServiceClient.getTasks(userId, "ACTIVE");
        boolean fatigue = isFatigued(userId);

        BigDecimal baselineHours = profile.dailyStudyHours() != null ? profile.dailyStudyHours() : BigDecimal.valueOf(4.0);
        BigDecimal effectiveHours = fatigue
                ? baselineHours.multiply(FATIGUE_REDUCTION_FACTOR).setScale(2, RoundingMode.HALF_UP)
                : baselineHours.setScale(2, RoundingMode.HALF_UP);

        List<ScoredTask> rankedTasks = tasks.stream()
                .filter(task -> !"DONE".equalsIgnoreCase(task.status()))
                .map(task -> scoreTask(task, planDate, fatigue))
                .sorted(Comparator.comparing(ScoredTask::score).reversed())
                .toList();

        List<PlannerDtos.StudyBlock> sessions = buildStudyBlocks(rankedTasks, effectiveHours, profile.preferredStartTime());
        List<String> rationale = buildRationale(fatigue, rankedTasks);

        return new PlannerDtos.DailyPlanResponse(
                userId,
                planDate,
                baselineHours.setScale(2, RoundingMode.HALF_UP),
                effectiveHours,
                fatigue,
                sessions,
                rationale
        );
    }

    public void publishFatigueUpdate(UUID userId, PlannerDtos.FatigueCommand command) {
        boolean tired = Boolean.TRUE.equals(command.tired());
        cacheFatigueState(userId, tired);
        PlannerDtos.FatigueUpdateEvent event = new PlannerDtos.FatigueUpdateEvent(
                UUID.randomUUID().toString(),
                "FATIGUE_UPDATED",
                OffsetDateTime.now(ZoneId.of(command.timezone() != null ? command.timezone() : "UTC")),
                userId,
                "web-frontend",
                new PlannerDtos.FatiguePayload(tired, command.reportedEnergyLevel(), command.note()),
                new PlannerDtos.EventContext(command.timezone(), LocalDate.now())
        );
        fatigueEventPublisher.publish(userId, event);
    }

    private ScoredTask scoreTask(PlannerDtos.TaskSummary task, LocalDate planDate, boolean fatigue) {
        long daysUntilDeadline = Math.max(ChronoUnit.DAYS.between(planDate, task.deadline()), 1);
        BigDecimal basePriority = BigDecimal.valueOf(task.difficulty())
                .multiply(task.weight())
                .divide(BigDecimal.valueOf(daysUntilDeadline), 3, RoundingMode.HALF_UP);

        BigDecimal effortMultiplier = BigDecimal.ONE.add(
                task.remainingHours().divide(BigDecimal.TEN, 3, RoundingMode.HALF_UP)
        );

        boolean fatigueAdjusted = fatigue && task.difficulty() >= 4;
        BigDecimal adjustedPriority = basePriority.multiply(effortMultiplier);
        if (fatigueAdjusted) {
            adjustedPriority = adjustedPriority.multiply(HIGH_DIFFICULTY_PENALTY);
        }

        return new ScoredTask(task, adjustedPriority.setScale(3, RoundingMode.HALF_UP), fatigueAdjusted);
    }

    private List<PlannerDtos.StudyBlock> buildStudyBlocks(List<ScoredTask> rankedTasks,
                                                          BigDecimal effectiveHours,
                                                          LocalTime preferredStart) {
        int availableMinutes = effectiveHours.multiply(BigDecimal.valueOf(60)).intValue();
        LocalTime cursor = preferredStart != null ? preferredStart : LocalTime.of(18, 0);
        List<PlannerDtos.StudyBlock> sessions = new ArrayList<>();
        int sessionOrder = 0;

        for (ScoredTask scoredTask : rankedTasks) {
            if (availableMinutes < 30) {
                break;
            }

            int remainingTaskMinutes = scoredTask.task().remainingHours().multiply(BigDecimal.valueOf(60)).intValue();
            if (remainingTaskMinutes <= 0) {
                continue;
            }

            int idealBlock = scoredTask.fatigueAdjusted() ? SHORT_BLOCK_MINUTES : DEFAULT_BLOCK_MINUTES;
            int blockMinutes = Math.min(Math.min(idealBlock, remainingTaskMinutes), availableMinutes);
            if (blockMinutes < 30) {
                continue;
            }

            LocalTime endTime = cursor.plusMinutes(blockMinutes);
            sessions.add(new PlannerDtos.StudyBlock(
                    scoredTask.task().id(),
                    scoredTask.task().subject(),
                    scoredTask.task().title(),
                    blockMinutes,
                    cursor,
                    endTime,
                    scoredTask.score(),
                    scoredTask.fatigueAdjusted()
            ));

            sessionOrder++;
            availableMinutes -= blockMinutes;
            cursor = endTime.plusMinutes(sessionOrder % 3 == 0 ? LONG_BREAK_MINUTES : SHORT_BREAK_MINUTES);
        }
        return sessions;
    }

    private List<String> buildRationale(boolean fatigue, List<ScoredTask> rankedTasks) {
        List<String> rationale = new ArrayList<>();
        rationale.add("Priority formula: (difficulty * weight) / max(daysUntilDeadline, 1)");
        if (fatigue) {
            rationale.add("Fatigue flag active: daily capacity reduced by 40% and difficulty 4-5 tasks penalized");
        }
        rankedTasks.stream()
                .limit(3)
                .forEach(task -> rationale.add(
                        "Selected " + task.task().title() + " with score " + task.score()
                                + (task.fatigueAdjusted() ? " after fatigue adjustment" : "")
                ));
        return rationale;
    }

    private boolean isFatigued(UUID userId) {
        return fatigueStateStore.isFatigued(userId);
    }

    private void cacheFatigueState(UUID userId, boolean tired) {
        fatigueStateStore.setFatigued(userId, tired);
    }

    private record ScoredTask(PlannerDtos.TaskSummary task, BigDecimal score, boolean fatigueAdjusted) {
    }
}
