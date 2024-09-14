# Local steps
# FROM alpine/java:21-jdk
# ARG JAR_FILE=build/libs/*jar
# CMD ["./gradlew", "clean", "build", "-x", "test"]
# COPY ./build/libs/tec.tac-toe-0.0.1-SNAPSHOT.jar app.jar
# ENTRYPOINT ["java","-jar","/app.jar"]

# Remote steps
# Stage 1: Build the application
FROM gradle:jdk21 as builder

# Set the working directory inside the container
WORKDIR /app

# Copy all project files to the container
COPY . .

# Make sure gradlew is executable
RUN chmod +x ./gradlew

# Run the Gradle build (including clean, build, and skipping tests)
RUN ./gradlew clean build -x test

# Stage 2: Create the final image
FROM eclipse-temurin:21-jdk-alpine

# Set the working directory
WORKDIR /app

# Copy the JAR file from the builder stage
COPY --from=builder /app/build/libs/tic-tac-toe-0.0.1-SNAPSHOT.jar app.jar

# Specify the entry point for the container
ENTRYPOINT ["java","-jar","app.jar"]