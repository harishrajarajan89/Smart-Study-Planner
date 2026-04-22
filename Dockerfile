# ── Stage 1: Build ────────────────────────────────────────────────────────────
FROM maven:3.9-eclipse-temurin-21 AS build

WORKDIR /app

# Copy parent POM first so Maven can resolve the multi-module reactor.
# Copying all module POMs before any source lets Docker cache the dependency
# download layer independently of source changes.
COPY pom.xml ./
COPY api-gateway/pom.xml        api-gateway/pom.xml
COPY discovery-server/pom.xml   discovery-server/pom.xml
COPY user-service/pom.xml       user-service/pom.xml
COPY task-service/pom.xml       task-service/pom.xml
COPY scheduler-engine/pom.xml   scheduler-engine/pom.xml

# Download all dependencies declared across the reactor.
# This layer is cached as long as no POM changes.
RUN mvn dependency:go-offline -B --no-transfer-progress

# Copy source for api-gateway and any modules it depends on at build time.
COPY api-gateway/src    api-gateway/src

# Build only api-gateway; --also-make resolves any intra-reactor deps.
RUN mvn clean package -pl api-gateway --also-make -DskipTests -B --no-transfer-progress

# ── Stage 2: Runtime ──────────────────────────────────────────────────────────
FROM eclipse-temurin:21-jre-jammy AS runtime

WORKDIR /app

# Create a non-root user for security.
RUN addgroup --system spring && adduser --system --ingroup spring spring

COPY --from=build /app/api-gateway/target/api-gateway-0.0.1-SNAPSHOT.jar app.jar

RUN chown spring:spring app.jar
USER spring

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
