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

## Known Issues
>  **Scheduler Engine** is currently not working as expected when running as part of the full service group. The service works fine in isolation but fails to integrate correctly with the other services. This is a known issue and will be resolved in a future update.