import { startTransition, useEffect, useState } from "react";
import { AnimatePresence, LayoutGroup, MotionConfig, motion } from "framer-motion";
import { taskApi, Task } from "./api";
import "./SmartStudyPlanner.css";

type UITask = {
  id: string;
  title: string;
  course: string;
  difficulty: number;
  urgency: number;
  energyCost: number;
  duration: number;
  status: "Due Soon" | "Deep Work" | "Quick Win";
};

type TaskCardProps = {
  task: UITask;
  priority: number;
  index: number;
  tiredMode: boolean;
  isFocusTarget?: boolean;
  compact?: boolean;
};

const tasksSeed: UITask[] = [
  {
    id: "task-algo",
    title: "Dynamic Programming Drill",
    course: "Algorithms",
    difficulty: 5,
    urgency: 5,
    energyCost: 5,
    duration: 90,
    status: "Due Soon",
  },
  {
    id: "task-bio",
    title: "Genetics Flash Review",
    course: "Biology",
    difficulty: 2,
    urgency: 4,
    energyCost: 2,
    duration: 25,
    status: "Quick Win",
  },
  {
    id: "task-econ",
    title: "Macro Case Summary",
    course: "Economics",
    difficulty: 3,
    urgency: 4,
    energyCost: 3,
    duration: 45,
    status: "Deep Work",
  },
  {
    id: "task-ui",
    title: "Interaction Design Critique",
    course: "Product Design",
    difficulty: 4,
    urgency: 3,
    energyCost: 4,
    duration: 60,
    status: "Deep Work",
  },
  {
    id: "task-stats",
    title: "Probability Quiz Retake",
    course: "Statistics",
    difficulty: 4,
    urgency: 5,
    energyCost: 4,
    duration: 40,
    status: "Due Soon",
  },
  {
    id: "task-notes",
    title: "Lecture Notes Compression",
    course: "History",
    difficulty: 2,
    urgency: 2,
    energyCost: 1,
    duration: 20,
    status: "Quick Win",
  },
];

function getPriority(task: UITask) {
  return task.difficulty * task.urgency;
}

function getTiredScore(task: UITask) {
  return getPriority(task) - task.energyCost * 2.2 + Math.max(0, 7 - task.duration / 10);
}

function getCardTier(priority: number) {
  if (priority >= 20) {
    return {
      className: "planner-task-card--hero",
    };
  }

  if (priority >= 12) {
    return {
      className: "planner-task-card--large",
    };
  }

  if (priority >= 7) {
    return {
      className: "planner-task-card--medium",
    };
  }

  return {
    className: "planner-task-card--small",
  };
}

function formatTime(totalSeconds: number) {
  const minutes = Math.floor(totalSeconds / 60)
    .toString()
    .padStart(2, "0");
  const seconds = (totalSeconds % 60).toString().padStart(2, "0");
  return `${minutes}:${seconds}`;
}

function sortTasks(tasks: UITask[], tiredMode: boolean) {
  const sorted = [...tasks];

  sorted.sort((left, right) => {
    const leftScore = tiredMode ? getTiredScore(left) : getPriority(left);
    const rightScore = tiredMode ? getTiredScore(right) : getPriority(right);

    if (rightScore !== leftScore) {
      return rightScore - leftScore;
    }

    return left.duration - right.duration;
  });

  return sorted;
}

function priorityGlow(priority: number, tiredMode: boolean) {
  if (priority >= 16) {
    return tiredMode ? "var(--planner-glow-cobalt)" : "var(--planner-glow-acid)";
  }

  if (priority >= 10) {
    return "0 0 0 1px rgba(255, 255, 255, 0.14), 0 12px 30px rgba(0, 0, 0, 0.28)";
  }

  return "0 0 0 1px rgba(255, 255, 255, 0.1), 0 14px 28px rgba(0, 0, 0, 0.22)";
}

export function TaskCard({
  task,
  priority,
  index,
  tiredMode,
  isFocusTarget = false,
  compact = false,
}: TaskCardProps) {
  const tier = getCardTier(priority);
  const isHighPriority = priority >= 16;
  const emphasisLabel = tiredMode ? "Energy-aware" : "Priority";

  return (
    <motion.article
      layout
      variants={{
        hidden: { opacity: 0, y: 20, scale: 0.97 },
        show: { opacity: 1, y: 0, scale: 1 },
      }}
      initial="hidden"
      animate="show"
      exit={{ opacity: 0, scale: 0.96, y: -12 }}
      transition={{
        duration: 0.45,
        ease: [0.22, 1, 0.36, 1],
        layout: {
          duration: 0.7,
          type: "spring",
          bounce: tiredMode ? 0.12 : 0.22,
          delay: index * 0.045,
        },
      }}
      className={[
        "planner-task-card",
        tier.className,
        compact ? "planner-task-card--compact" : "",
      ].join(" ")}
      style={{
        backgroundColor: "var(--planner-panel)",
        borderColor: isHighPriority ? "rgba(210, 255, 63, 0.28)" : "var(--planner-border)",
        boxShadow: priorityGlow(priority, tiredMode),
        animation: isHighPriority && !isFocusTarget ? "plannerPulse 2.8s ease-in-out infinite" : undefined,
      }}
    >
      <div className="planner-task-card__halo" />

      <div className="planner-task-card__content">
        <div className="planner-task-card__top">
          <div className="planner-task-card__header">
            <div>
              <p className="planner-task-card__course">{task.course}</p>
              <h3 className="planner-task-card__title">{task.title}</h3>
            </div>

            <span className="planner-task-card__status">{task.status}</span>
          </div>

          <div className="planner-metrics">
            <Metric label="Difficulty" value={task.difficulty} />
            <Metric label="Urgency" value={task.urgency} />
            <Metric label="Energy" value={task.energyCost} />
          </div>
        </div>

        <div className="planner-task-card__footer">
          <div>
            <p className="planner-task-card__priority-label">{emphasisLabel}</p>
            <p className="planner-task-card__priority-value">{priority}</p>
          </div>

          <div className="planner-task-card__block">
            <p className="planner-task-card__block-label">Block</p>
            <p className="planner-task-card__block-value">{task.duration} min</p>
          </div>
        </div>
      </div>
    </motion.article>
  );
}

function Metric({ label, value }: { label: string; value: number }) {
  return (
    <div className="planner-metric">
      <p className="planner-metric__label">{label}</p>
      <p className="planner-metric__value">{value}</p>
    </div>
  );
}

function LiquidFatigueToggle({
  tiredMode,
  onToggle,
}: {
  tiredMode: boolean;
  onToggle: () => void;
}) {
  return (
    <motion.button
      type="button"
      onClick={onToggle}
      aria-pressed={tiredMode}
      whileTap={{ scale: 0.98 }}
      className={["planner-toggle", tiredMode ? "is-tired" : ""].join(" ")}
    >
      <motion.div
        aria-hidden
        animate={{
          x: tiredMode ? "88%" : "0%",
          scale: tiredMode ? 1.04 : 0.98,
          borderRadius: tiredMode ? "30px" : "24px",
        }}
        transition={{ type: "spring", stiffness: 220, damping: 22 }}
        className="planner-toggle__blob"
      />

      <div className="planner-toggle__content">
        <div className="planner-toggle__copy">
          <p className="planner-eyebrow planner-eyebrow--muted">Energy slider</p>
          <p className="planner-toggle__title">{tiredMode ? "I'm tired, lighten the load" : "I'm locked in, show the hard stuff"}</p>
        </div>

        <div className="planner-toggle__meta">
          <p className="planner-eyebrow planner-eyebrow--muted">Mode</p>
          <p className="planner-toggle__value">{tiredMode ? "Gravity On" : "Flow State"}</p>
        </div>
      </div>
    </motion.button>
  );
}

function FocusFlow({
  task,
  countdown,
  onExit,
}: {
  task: UITask;
  countdown: number;
  onExit: () => void;
}) {
  return (
    <motion.section
      key="focus-flow"
      initial={{ opacity: 0, scale: 0.98 }}
      animate={{ opacity: 1, scale: 1 }}
      exit={{ opacity: 0, scale: 0.985 }}
      transition={{ duration: 0.35, ease: [0.22, 1, 0.36, 1] }}
      className="planner-focus"
    >
      <div className="planner-focus__grid">
        <div className="planner-focus__pane">
          <div>
            <p className="planner-eyebrow planner-eyebrow--muted">Focus Flow</p>
            <h2 className="planner-focus__headline">One task. No clutter.</h2>
          </div>

          <div>
            <p className="planner-focus__timer-label">Countdown</p>
            <p className="planner-focus__timer">{formatTime(countdown)}</p>
          </div>
        </div>

        <div className="planner-focus__pane">
          <div>
            <p className="planner-eyebrow planner-eyebrow--muted">{task.course}</p>
            <h3 className="planner-focus__task-title">{task.title}</h3>
            <p className="planner-focus__copy">
              Brutalist timer up front, every other signal pushed away. This view is intentionally ruthless about reducing context switching.
            </p>
          </div>

          <div className="planner-focus__metrics">
            <Metric label="Priority" value={getPriority(task)} />
            <Metric label="Energy" value={task.energyCost} />
            <Metric label="Minutes" value={task.duration} />
          </div>

          <button
            type="button"
            onClick={onExit}
            className="planner-action"
          >
            Exit Focus Flow
          </button>
        </div>
      </div>
    </motion.section>
  );
}

export default function SmartStudyPlannerDemo() {
  const [tiredMode, setTiredMode] = useState(false);
  const [focusFlow, setFocusFlow] = useState(false);
  const [countdown, setCountdown] = useState(25 * 60);
  const [tasks, setTasks] = useState<UITask[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  // Hardcoded user ID for demo - in a real app this would come from authentication
  const userId = "550e8400-e29b-41d4-a716-446655440000";

  useEffect(() => {
    loadTasks();
  }, []);

  const loadTasks = async () => {
    try {
      setLoading(true);
      setError(null);
      const fetchedTasks = await taskApi.getTasks(userId);
      // Map API response to UI format
      const mappedTasks: UITask[] = fetchedTasks.map(apiTask => ({
        id: apiTask.id,
        title: apiTask.title,
        course: apiTask.subject,
        difficulty: apiTask.difficulty,
        urgency: Math.max(1, Math.min(5, Math.round(apiTask.weight))), // Convert weight to 1-5 scale
        energyCost: Math.max(1, Math.min(5, Math.round(apiTask.effortScore / 2))), // Convert effort score
        duration: Math.round(apiTask.estimatedHours * 60), // Convert hours to minutes
        status: apiTask.status === 'PENDING' ? 'Due Soon' :
                apiTask.progressPercent > 50 ? 'Deep Work' : 'Quick Win'
      }));
      setTasks(mappedTasks);
    } catch (err) {
      console.error('Failed to load tasks:', err);
      setError('Failed to load tasks. Using sample data.');
      // Fallback to sample data if API fails
      setTasks(tasksSeed);
    } finally {
      setLoading(false);
    }
  };

  const orderedTasks = sortTasks(tasks, tiredMode);
  const focusTask = orderedTasks[0];

  useEffect(() => {
    if (!focusFlow) {
      return;
    }

    setCountdown(25 * 60);

    const timer = window.setInterval(() => {
      setCountdown((previous) => {
        if (previous <= 1) {
          return 25 * 60;
        }

        return previous - 1;
      });
    }, 1000);

    return () => {
      window.clearInterval(timer);
    };
  }, [focusFlow]);

  function handleToggleTiredMode() {
    startTransition(() => {
      setTiredMode((previous) => !previous);
    });
  }

  return (
    <MotionConfig transition={{ duration: 0.6, ease: [0.22, 1, 0.36, 1] }}>
      <div className="planner-shell">
        <AnimatePresence mode="wait">
          {focusFlow ? (
            <FocusFlow task={focusTask} countdown={countdown} onExit={() => setFocusFlow(false)} />
          ) : (
            <motion.main
              key="planner-grid"
              initial={{ opacity: 0, y: 18 }}
              animate={{ opacity: 1, y: 0 }}
              exit={{ opacity: 0, y: -18 }}
              className="planner-shell__inner planner-stack"
            >
              <section className="planner-grid planner-grid--hero">
                <div className="planner-panel">
                  <div className="planner-panel__content">
                    <p className="planner-eyebrow planner-eyebrow--accent">Smart Study Planner</p>
                    <div className="planner-hero">
                      <div className="planner-hero__copy">
                        <h1 className="planner-title">Neo-bento planning with energy-aware gravity.</h1>
                        <p className="planner-copy">
                        Cards carry visual mass based on difficulty times urgency. When tired mode turns on, the schedule physically slides toward lower-energy wins instead of merely flipping a checkbox.
                        </p>
                      </div>

                      <button type="button" onClick={() => setFocusFlow(true)} className="planner-action">
                        Enter Focus Flow
                      </button>
                    </div>
                  </div>
                </div>

                <div className="planner-panel planner-panel--soft">
                  <div className="planner-panel__content planner-sidecard">
                    <p className="planner-eyebrow planner-eyebrow--muted">Now</p>
                    <p className="planner-sidecard__label">Current top task</p>
                    <TaskCard
                      task={focusTask}
                      priority={getPriority(focusTask)}
                      tiredMode={tiredMode}
                      index={0}
                      isFocusTarget
                      compact
                    />
                  </div>
                </div>
              </section>

              <section className="planner-grid planner-grid--content">
                <div className="planner-sidebar">
                  <LiquidFatigueToggle tiredMode={tiredMode} onToggle={handleToggleTiredMode} />

                  <div className="planner-note">
                    <p className="planner-eyebrow planner-eyebrow--muted">Re-sort logic</p>
                    <p className="planner-note__copy">
                      Awake mode sorts by <strong>difficulty * urgency</strong>. Tired mode subtracts an energy penalty, so quick wins and medium-weight tasks rise while the layout animates into a new order.
                    </p>
                  </div>
                </div>

                <LayoutGroup id="planner-grid">
                  <motion.section
                    layout
                    variants={{
                      hidden: {},
                      show: {
                        transition: {
                          staggerChildren: 0.06,
                          delayChildren: 0.03,
                        },
                      },
                    }}
                    initial="hidden"
                    animate="show"
                    className="planner-task-grid"
                  >
                    <AnimatePresence mode="popLayout">
                      {loading ? (
                        <motion.div
                          initial={{ opacity: 0 }}
                          animate={{ opacity: 1 }}
                          className="planner-loading"
                        >
                          <p>Loading your study plan...</p>
                        </motion.div>
                      ) : error ? (
                        <motion.div
                          initial={{ opacity: 0 }}
                          animate={{ opacity: 1 }}
                          className="planner-error"
                        >
                          <p>{error}</p>
                          <button onClick={loadTasks} className="planner-action">
                            Retry
                          </button>
                        </motion.div>
                      ) : (
                        orderedTasks.map((task, index) => (
                          <TaskCard
                            key={task.id}
                            task={task}
                            priority={getPriority(task)}
                            tiredMode={tiredMode}
                            index={index}
                          />
                        ))
                      )}
                    </AnimatePresence>
                  </motion.section>
                </LayoutGroup>
              </section>
            </motion.main>
          )}
        </AnimatePresence>
      </div>
    </MotionConfig>
  );
}
