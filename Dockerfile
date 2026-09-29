# Stage 1 — Build fat JAR
FROM maven:3.9-eclipse-temurin-17-alpine AS builder
WORKDIR /build

# Cache dependency layer separately from source
COPY pom.xml .
RUN mvn dependency:go-offline -q

COPY src ./src
RUN mvn package -DskipTests -q && \
    cp target/dw-cloudinary-*.jar target/app.jar

# Stage 2 — Minimal runtime image
FROM eclipse-temurin:17-jre-alpine

RUN addgroup -S appgroup && adduser -S appuser -G appgroup
WORKDIR /app

COPY --from=builder /build/target/app.jar app.jar
RUN chown appuser:appgroup app.jar

USER appuser

ENV JAVA_OPTS="-Xms128m -Xmx384m \
               -XX:MaxMetaspaceSize=128m \
               -XX:ReservedCodeCacheSize=32m \
               -XX:MaxDirectMemorySize=32m \
               -Xss512k \
               -XX:ActiveProcessorCount=2 \
               -XX:+UseG1GC \
               -Djava.security.egd=file:/dev/./urandom"

CMD ["sh", "-c", "java $JAVA_OPTS -Dserver.port=${PORT:-8080} -Dspring.profiles.active=${SPRING_PROFILES_ACTIVE:-prod} -jar app.jar"]
