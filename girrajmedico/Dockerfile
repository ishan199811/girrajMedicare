# Use a lightweight OpenJDK base image for the build
FROM eclipse-temurin:17-jdk-jammy

# Set the working directory inside the container
WORKDIR /app

# Copy the executable JAR file from your build output to the container
# NOTE: Replace 'your-app-name.jar' with the actual name of your Spring Boot JAR file
COPY target/your-app-name.jar app.jar

# Expose the port that the application will run on
# Spring Boot's default port is 8080
EXPOSE 8080

# Define the command to run the application
# This command will be executed when the container starts
CMD ["java", "-jar", "app.jar"]
