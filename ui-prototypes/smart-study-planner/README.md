# Smart Study Planner Prototype

This folder contains a standalone React/Tailwind/Framer Motion prototype for the requested premium "Smart Study Planner" UI.

Files:

- `theme.css`: design tokens and animation keyframes for the Neo-Bento palette.
- `SmartStudyPlanner.tsx`: a drop-in demo that includes:
  - adaptive task cards sized by `difficulty * urgency`
  - a liquid "Tired Mode" toggle
  - staggered, animated card re-sorting with Framer Motion layout transitions
  - a minimal Focus Flow view with a brutalist countdown timer

Usage notes:

- Import `theme.css` once near your app root.
- Render `SmartStudyPlannerDemo` inside any React app that already has Tailwind CSS and `framer-motion` installed.
- The re-sort animation is driven by `layout` transitions plus per-card `delay` values based on the new index, so the schedule appears to physically settle into place instead of snapping.
