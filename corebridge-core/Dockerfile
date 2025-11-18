FROM eclipse-temurin:17-jdk

WORKDIR /app

# 멀티모듈에서 corebridge-core 모듈 JAR만 복사
COPY build/libs/*.jar app.jar

ENV SPRING_PROFILES_ACTIVE=prod

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
