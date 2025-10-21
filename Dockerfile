FROM openjdk:17-jdk-slim

WORKDIR /app

# Copy Maven files
COPY pom.xml .
COPY src ./src

# Install Maven
RUN apt-get update && apt-get install -y maven && rm -rf /var/lib/apt/lists/*

# Build application
RUN mvn clean package -DskipTests

# Expose port
EXPOSE 8080

# Run Spring Boot application
CMD ["java", "-jar", "target/uml-to-code-generator-1.0.0.jar"]