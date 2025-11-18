# Stage 1: Runtime only (JAR은 외부에서 빌드)
FROM eclipse-temurin:17-jre-alpine

# 애플리케이션 사용자 생성
RUN addgroup -S spring && adduser -S spring -G spring

WORKDIR /app

# corebridge-batch JAR 복사
# (로컬에서 빌드한 JAR을 Docker build 시 COPY)
COPY build/libs/*.jar app.jar

# 업로드 디렉토리 생성
RUN mkdir -p /app/uploads && \
    chown -R spring:spring /app

# Switch to non-root user
USER spring:spring

# JVM 옵션 설정
ENV JAVA_OPTS="-Xms256m -Xmx512m -XX:+UseG1GC -XX:MaxGCPauseMillis=200"

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=10s --retries=1 \
  CMD pgrep -f java || exit 1

# Run application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/app.jar"]

# Labels
LABEL maintainer="corebridge-team"
LABEL version="1.0"
LABEL description="CoreBridge Notification Batch Service"
