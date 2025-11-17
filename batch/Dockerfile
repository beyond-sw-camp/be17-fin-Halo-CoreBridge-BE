FROM openjdk:17-jdk-slim

WORKDIR /app

# JAR 파일 복사
COPY core-bridge-batch/build/libs/corebridge-batch.jar app.jar

# 포트 설정 (모니터링용)
EXPOSE 8081

# 실행
ENTRYPOINT ["java", \
    "-XX:+UseContainerSupport", \
    "-XX:MaxRAMPercentage=75.0", \
    "-Djava.security.egd=file:/dev/./urandom", \
    "-jar", \
    "/app/app.jar"]
