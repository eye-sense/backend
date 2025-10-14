# Multi-stage Dockerfile for Spring Boot application
FROM maven:3.9.6-eclipse-temurin-21 AS build

WORKDIR /workspace

COPY pom.xml .
COPY src ./src

RUN mvn -B clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

RUN addgroup -g 1001 -S eyesense && \
    adduser -S -D -H -u 1001 -s /sbin/nologin -G eyesense eyesense

COPY --from=build /workspace/target/ai-app-*.jar /app/app.jar

RUN chown -R eyesense:eyesense /app

USER eyesense

EXPOSE 8090

ENV JAVA_OPTS="-Xmx512m -Xms256m -Djava.security.egd=file:/dev/./urandom"

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
