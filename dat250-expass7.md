# Dockerfile
- This Dockerfile uses a multi-stage build to containerize the Spring Boot app. 

## First stage
- The first stage (gradle:8.9-jdk21) compiles the application inside a Gradle + Java 21 environment. Images are pulled from dockerhub by default. 
- It sets the working directory, copies the Gradle files to cache dependencies, pre-downloads them for faster builds, copies all source files, and runs bootJar to create the executable JAR.

### --no-daemon 
- Gradle normally runs "daemon process" in background.`
- `--no-daemon` tells: Run in the foreground, do the build, then exit. Don’t stay running in the background.
- In a Docker container, this is usually preferred because containers are short-lived. They typically run one build and then stop.
- Less resource usage, No lingering background processes that could cause permission issues or file locks.

### Gradle bootJar
- The bootJar task is a Spring Boot specific Gradle task that produces a self-container executable JAR, which contains:
1. all compiled .class files from source code. 
2. 2.Include all resource files. 
3. Packages a Java runtime launcher. `java -jar app.jar`.
- Result/snapshot is located: `build/libs/pollapp-0.0.1-SNAPSHOT.jar`

## Second stage
- The second stage (eclipse-temurin:21-jdk-alpine) is a runtime image with Java 21, where a non-root user is created for security. 
- The built JAR is copied from the builder stage, port 8080 is exposed, and the container starts the app with `java -jar app.jar`.

## Build image & Run application
`docker build -t pollapp-java21 .`