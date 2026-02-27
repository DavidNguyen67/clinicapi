# Multi-stage build cho Spring Boot application

# Stage 1: Build stage
FROM maven:3.9-eclipse-temurin-21-alpine AS builder

WORKDIR /app

# Copy pom.xml và tải dependencies trước (cache layer)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source code và build
COPY src ./src
RUN mvn clean package -DskipTests -B

# Stage 2: Runtime stage
FROM eclipse-temurin:21-jre-alpine AS runner

WORKDIR /app

# Tạo user non-root để chạy app
RUN addgroup --system --gid 1001 spring && \
    adduser --system --uid 1001 --ingroup spring springboot

# Copy JAR file từ build stage
COPY --from=builder --chown=springboot:spring /app/target/*.jar app.jar

# Chuyển sang user non-root
USER springboot

# Expose port
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
    CMD wget --no-verbose --tries=1 --spider http://localhost:8080/actuator/health || exit 1

# JVM options để optimize memory
ENV JAVA_OPTS="-Xmx512m -Xms256m -XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"

# Chạy application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
