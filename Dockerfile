# =======================================
# 1️ BUILD STAGE - uses Gradle to build the app
# =======================================
FROM gradle:8.9-jdk21 AS builder

WORKDIR /app

# Copy Gradle files first for caching dependencies
COPY build.gradle.kts settings.gradle.kts gradlew ./
COPY gradle gradle

# Pre-download dependencies
RUN ./gradlew dependencies --no-daemon

# Copy all source code
COPY . .

# Build the Spring Boot executable jar
RUN ./gradlew bootJar --no-daemon --parallel

# =======================================
# 2️ RUNTIME STAGE - runs the built JAR
# =======================================
FROM eclipse-temurin:21-jdk-alpine

# Create a non-root user
RUN addgroup --system spring && adduser --system spring --ingroup spring
USER spring:spring

WORKDIR /app

# Copy the built JAR from the builder stage
COPY --from=builder /app/build/libs/*.jar app.jar

# Expose Spring Boot's default port
EXPOSE 8080

# Start the application
CMD ["java", "-jar", "app.jar"]
