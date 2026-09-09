# Stage 1: Build the application
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

# Copy the pom.xml file and download dependencies first (for caching)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy the actual source code and build the application
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Create the lightweight runtime image
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Copy the built jar file from the build stage
COPY --from=build /app/target/*.jar app.jar

# Expose the port your Spring Boot app runs on
EXPOSE 8081

# Command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
