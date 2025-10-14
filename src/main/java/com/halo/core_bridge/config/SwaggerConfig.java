package com.halo.core_bridge.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {

        return new OpenAPI()
                .info(new Info()
                        .title("Core Bridge API Docs")
                        .description("Halo Core Bridge 프로젝트의 REST API 문서")
                        .version("v1.0.0")
                        .contact(new Contact()
                                .name("HALO Dev Team")
                                .email("com.corebridge@gmail.com")
                                .url("https://corebridge.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0.html")))
                .servers(List.of(
                        new Server().url("http://localhost:8080").description("Local Server"),
                        new Server().url("https://api.halo.com").description("Production Server")
                ))
                .externalDocs(new ExternalDocumentation()
                        .description("GitHub Repository")
                        .url("https://github.com/beyond-sw-camp/be17-fin-Halo-CoreBridge-BE"));
    }
}
