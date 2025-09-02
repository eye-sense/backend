# Simple Dockerfile for Spring Boot application
FROM eclipse-temurin:21-jre-alpine

# Set working directory
WORKDIR /app

# Create a non-root user for security
RUN addgroup -g 1001 -S eyesense && \
    adduser -S -D -H -u 1001 -s /sbin/nologin -G eyesense eyesense

# Copy the built JAR file
# Note: Build the JAR with: mvn clean package -DskipTests
COPY target/ai-app-*.jar app.jar

# Change ownership of the app directory
RUN chown -R eyesense:eyesense /app

# Switch to non-root user
USER eyesense

# Expose the port the app runs on
EXPOSE 8090

# Set JVM options for container environment
ENV JAVA_OPTS="-Xmx512m -Xms256m -Djava.security.egd=file:/dev/./urandom"

# Run the application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]