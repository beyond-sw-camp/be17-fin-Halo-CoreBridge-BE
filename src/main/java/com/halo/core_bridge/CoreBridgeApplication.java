package com.halo.core_bridge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
@EnableElasticsearchRepositories(basePackages = "com.halo.core_bridge")
public class CoreBridgeApplication {

	public static void main(String[] args) {
		SpringApplication.run(CoreBridgeApplication.class, args);
	}

}
