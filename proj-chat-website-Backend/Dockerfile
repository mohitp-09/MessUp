# Stage 1: Build the Spring Boot application using Maven
FROM maven:3.8.5-openjdk-17 AS build

WORKDIR /app

# Copy everything from backend directory
COPY . .

# Build the application
RUN mvn clean package -DskipTests

# Stage 2: Use a lighter image to run the app
FROM openjdk:17-jdk-slim

WORKDIR /app

# Copy the built JAR from the previous stage
COPY --from=build /app/target/*.jar app.jar

# Expose the port your app runs on
EXPOSE 8080

# Run the application
CMD ["java", "-jar", "app.jar"]
