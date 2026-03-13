package com.halo.core_bridge.api.jobposting.model.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TechStack {
    // Backend
    SPRING_BOOT("Spring Boot"),
    SPRING_MVC("Spring MVC"),
    SPRING_SECURITY("Spring Security"),
    JAVA("Java"),
    KOTLIN("Kotlin"),
    NODE_JS("Node.js"),
    EXPRESS("Express.js"),
    PYTHON("Python"),
    DJANGO("Django"),

    // Frontend
    TYPESCRIPT("TypeScript"),
    JAVASCRIPT("JavaScript"),
    VUE("Vue.js"),
    REACT("React"),
    HTML("HTML5"),
    CSS("CSS3"),
    TAILWIND("Tailwind CSS"),
    HTML_CSS("HTML/CSS"),
    WEBPACK("Webpack"),

    // Database
    MYSQL("MySQL"),
    MARIADB("MariaDB"),
    POSTGRESQL("PostgreSQL"),
    MONGODB("MongoDB"),
    REDIS("Redis"),
    ELASTICSEARCH("Elasticsearch"),
    SQL("SQL"),

    // DevOps / Infra
    AWS("AWS"),
    DOCKER("Docker"),
    KUBERNETES("Kubernetes"),
    GITHUB_ACTIONS("GitHub Actions"),
    JENKINS("Jenkins"),
    NGINX("Nginx"),
    APACHE("Apache HTTP Server"),
    LINUX("Linux"),
    GIT("Git"),

    // Messaging / Streaming
    KAFKA("Kafka"),
    RABBITMQ("RabbitMQ"),

    // Data / Analytics
    SPARK("Apache Spark"),
    AIRFLOW("Apache Airflow"),
    ETL("ETL"),
    GOOGLE_ADS("Google Ads"),
    FACEBOOK_ADS("Facebook Ads"),
    GOOGLE_ANALYTICS("Google Analytics"),
    EXCEL("Excel"),

    // Testing
    JUNIT("JUnit5"),
    MOCKITO("Mockito"),
    CYPRESS("Cypress"),
    PLAYWRIGHT("Playwright"),

    // Mobile / Cross Platform
    FLUTTER("Flutter"),
    REACT_NATIVE("React Native"),
    ANDROID_STUDIO("Android Studio"),
    JETPACK("Jetpack"),

    // Collaboration / Tools
    FIGMA("Figma"),
    POSTMAN("Postman"),
    SWAGGER("Swagger / OpenAPI"),
    SKETCH("Sketch"),
    PHOTOSHOP("Photoshop"),
    ILLUSTRATOR("Illustrator"),
    PROTOTYPING("Prototyping");

    private final String label;
}