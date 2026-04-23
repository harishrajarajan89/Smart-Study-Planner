const API_BASE_URL = (
  import.meta.env.VITE_API_BASE_URL || "http://localhost:8080"
).replace(/\/$/, "");

export interface Task {
  id: string;
  userId: string;
  subject: string;
  title: string;
  description: string;
  deadline: string;
  difficulty: number;
  weight: number;
  effortScore: number;
  estimatedHours: number;
  remainingHours: number;
  progressPercent: number;
  status: string;
}

export interface EnergyProfile {
  userId: string;
  displayName: string;
  timezone: string;
  dailyStudyHours: number;
  baselineEnergyLevel: number;
  preferredStartTime: string;
  preferredEndTime: string;
}

export interface StudyBlock {
  taskId: string;
  subject: string;
  title: string;
  blockMinutes: number;
  plannedStart: string;
  plannedEnd: string;
  priorityScore: number;
  fatigueAdjusted: boolean;
}

export interface DailyPlan {
  userId: string;
  planDate: string;
  baselineHours: number;
  effectiveHours: number;
  fatigueApplied: boolean;
  sessions: StudyBlock[];
  rationale: string[];
}

// Task API
export const taskApi = {
  getTasks: async (userId: string, status?: string): Promise<Task[]> => {
    const params = new URLSearchParams({ userId });
    if (status) params.append('status', status);

    const response = await fetch(`${API_BASE_URL}/api/tasks?${params}`);
    if (!response.ok) throw new Error('Failed to fetch tasks');
    return response.json();
  },

  getTask: async (taskId: string): Promise<Task> => {
    const response = await fetch(`${API_BASE_URL}/api/tasks/${taskId}`);
    if (!response.ok) throw new Error('Failed to fetch task');
    return response.json();
  },

  createTask: async (task: Omit<Task, 'id'>): Promise<Task> => {
    const response = await fetch(`${API_BASE_URL}/api/tasks`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(task),
    });
    if (!response.ok) throw new Error('Failed to create task');
    return response.json();
  },

  updateTask: async (taskId: string, task: Partial<Task>): Promise<Task> => {
    const response = await fetch(`${API_BASE_URL}/api/tasks/${taskId}`, {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(task),
    });
    if (!response.ok) throw new Error('Failed to update task');
    return response.json();
  },
};

// User API
export const userApi = {
  getEnergyProfile: async (userId: string): Promise<EnergyProfile> => {
    const response = await fetch(`${API_BASE_URL}/api/users/${userId}/energy-profile`);
    if (!response.ok) throw new Error('Failed to fetch energy profile');
    return response.json();
  },

  updateEnergyProfile: async (userId: string, profile: Partial<EnergyProfile>): Promise<EnergyProfile> => {
    const response = await fetch(`${API_BASE_URL}/api/users/${userId}/energy-profile`, {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(profile),
    });
    if (!response.ok) throw new Error('Failed to update energy profile');
    return response.json();
  },
};

// Planner API
export const plannerApi = {
  getTodayPlan: async (userId: string, date?: string): Promise<DailyPlan> => {
    const params = new URLSearchParams({ userId });
    if (date) params.append('date', date);

    const response = await fetch(`${API_BASE_URL}/api/plans/today?${params}`);
    if (!response.ok) throw new Error('Failed to fetch daily plan');
    return response.json();
  },

  recalculatePlan: async (userId: string, date?: string): Promise<DailyPlan> => {
    const params = new URLSearchParams({ userId });
    if (date) params.append('date', date);

    const response = await fetch(`${API_BASE_URL}/api/plans/recalculate?${params}`, {
      method: 'POST',
    });
    if (!response.ok) throw new Error('Failed to recalculate plan');
    return response.json();
  },

  updateFatigue: async (
    userId: string,
    payload: {
      tired: boolean;
      reportedEnergyLevel?: number;
      note?: string;
      timezone?: string;
    },
  ): Promise<DailyPlan> => {
    const response = await fetch(`${API_BASE_URL}/api/plans/fatigue?userId=${userId}`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(payload),
    });
    if (!response.ok) throw new Error('Failed to update fatigue');
    return response.json();
  },
};
