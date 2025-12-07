FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

# Install Maven
RUN apk add --no-cache maven

# Copy Maven files
COPY pom.xml .
COPY src ./src

# Build application
RUN mvn clean package -DskipTests

# Expose port
EXPOSE 8080

# Run Spring Boot application
CMD ["java", "-jar", "target/generator-1.0.0.jar"]