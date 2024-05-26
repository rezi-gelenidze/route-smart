# Use a Maven image to build the application
FROM maven:3.9.6-eclipse-temurin-17-alpine as build

# Set the working directory in the container
WORKDIR /app

# Copy the pom.xml and the src directory to the working directory
COPY pom.xml .
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests

# Use an official OpenJDK runtime as a parent image for the final image
FROM openjdk:17-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Install netcat for wait-for-db script
RUN apt-get update && apt-get install -y netcat && rm -rf /var/lib/apt/lists/*

# Copy the executable jar file from the build stage into the container
COPY --from=build /app/target/route-smart-0.0.1-SNAPSHOT.jar app.jar

# Copy entrypoint script
COPY entrypoint.sh /entrypoint.sh
RUN chmod +x /entrypoint.sh

# Make port 8080 available to the world outside this container
EXPOSE 8080

# Default command to run the jar file
ENTRYPOINT ["/entrypoint.sh"]

