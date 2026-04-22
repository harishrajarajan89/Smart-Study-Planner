# Railway Docker Deployment

This repo now includes one Dockerfile per backend service:

- `Dockerfile.discovery-server`
- `Dockerfile.api-gateway`
- `Dockerfile.user-service`
- `Dockerfile.task-service`
- `Dockerfile.scheduler-engine`

For every Railway service:

- Set `Root Directory` to `.`
- Do not set custom build or start commands
- Add `RAILWAY_DOCKERFILE_PATH` to point at the matching Dockerfile

## Recommended deploy order

1. `discovery-server`
2. `user-service`
3. `task-service`
4. `scheduler-engine`
5. `api-gateway`

## discovery-server

- `RAILWAY_DOCKERFILE_PATH=Dockerfile.discovery-server`
- `PORT=8761`

## user-service

- `RAILWAY_DOCKERFILE_PATH=Dockerfile.user-service`
- `PORT=8081`
- `EUREKA_URL=http://<discovery-private-domain>:8761/eureka`
- `DATABASE_URL=jdbc:postgresql://<host>:<port>/<database>`
- `DATABASE_USERNAME=<username>`
- `DATABASE_PASSWORD=<password>`

## task-service

- `RAILWAY_DOCKERFILE_PATH=Dockerfile.task-service`
- `PORT=8082`
- `EUREKA_URL=http://<discovery-private-domain>:8761/eureka`
- `DATABASE_URL=jdbc:postgresql://<host>:<port>/<database>`
- `DATABASE_USERNAME=<username>`
- `DATABASE_PASSWORD=<password>`

## scheduler-engine

- `RAILWAY_DOCKERFILE_PATH=Dockerfile.scheduler-engine`
- `PORT=8083`
- `EUREKA_URL=http://<discovery-private-domain>:8761/eureka`
- `APP_FATIGUE_STORE=memory`
- `APP_FATIGUE_PUBLISHER=none`

## api-gateway

- `RAILWAY_DOCKERFILE_PATH=Dockerfile.api-gateway`
- `PORT=8080`
- `EUREKA_URL=http://<discovery-private-domain>:8761/eureka`

Only `api-gateway` should have a public domain.
