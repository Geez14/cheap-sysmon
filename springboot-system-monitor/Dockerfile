FROM openjdk:17-jdk-slim

WORKDIR /app

# Copy the built JAR file
COPY target/springboot-system-monitor-1.0.0.jar .

# Expose port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "springboot-system-monitor-1.0.0.jar"]

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=10s --retries=3 \
    CMD curl -f http://localhost:8080/api/system/health || exit 1
