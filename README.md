# Study Planner Project

## Overview
The Study Planner project is a microservices-based application for managing study schedules.

## Components
- **Discovery Server**: Service registry.
- **API Gateway**: Routes client requests.
- **User Service**: Manages user data.
- **Task Service**: Handles tasks.
- **Scheduler Engine**: Manages study sessions.
- **Frontend**: User interface.

## Quick Start
1. Build backend services:
   ```bash
   mvn clean install
   ```
2. Start services:
   ```bash
   docker-compose -f docker-compose.full.yml up --build
   ```
3. Start frontend:
   ```bash
   cd ui-prototypes/study-planner-ui
   npm install
   npm run dev
   ```