import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import './index.css'
import './theme.css'
import SmartStudyPlannerDemo from './SmartStudyPlanner'

createRoot(document.getElementById('root')!).render(
  <StrictMode>
    <SmartStudyPlannerDemo />
  </StrictMode>,
)
