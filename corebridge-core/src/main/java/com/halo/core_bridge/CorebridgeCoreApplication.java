package com.halo.core_bridge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class CorebridgeCoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(CorebridgeCoreApplication.class, args);
    }

}
