package com.halo.core_bridge.config;

import com.halo.core_bridge.api.token.filter.LoginFilter;
import com.halo.core_bridge.common.model.BaseResponse;
import com.halo.core_bridge.common.model.BaseResponseStatus;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.media.*;
import io.swagger.v3.oas.models.parameters.RequestBody;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.servers.Server;
import org.apache.commons.lang3.ObjectUtils;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.SecurityFilterChain;

import java.util.List;
import java.util.Optional;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenApiCustomizer springSecurityLoginEndpointCustomizer(ApplicationContext applicationContext) {
        FilterChainProxy springSecurityFilterChain =
                applicationContext.getBean("springSecurityFilterChain", FilterChainProxy.class);

        return openApi -> {
            for (SecurityFilterChain filterChain : springSecurityFilterChain.getFilterChains()) {
                Optional<LoginFilter> filter = filterChain.getFilters().stream()
                        .filter(LoginFilter.class::isInstance)
                        .map(LoginFilter.class::cast)
                        .findAny();

                if (filter.isPresent()) {
                    Operation operation = new Operation();

                    // 요청 스키마
                    Schema<?> requestSchema = new ObjectSchema()
                            .addProperty("email", new StringSchema().example("user@example.com"))
                            .addProperty("password", new StringSchema().example("1234"));
                    RequestBody requestBody = new RequestBody().content(
                            new Content().addMediaType("application/json", new MediaType().schema(requestSchema))
                    );
                    operation.setRequestBody(requestBody);

                    // ✅ BaseResponse 구조 반영한 응답 스키마

                    Schema<?> baseResponseSchema = new ObjectSchema()
                            .addProperty("isSuccess", new BooleanSchema().example(true))
                            .addProperty("code", new StringSchema().example(BaseResponseStatus.SUCCESS.getCode()))
                            .addProperty("message", new StringSchema().example("요청에 성공하였습니다."))
                            .addProperty("result", null);

                    Schema<?> baseResponseSchemaError = new ObjectSchema()
                            .addProperty("isSuccess", new BooleanSchema().example(true))
                            .addProperty("code", new StringSchema().example(BaseResponseStatus.INVALID_USER_INFO.getCode()))
                            .addProperty("message", new StringSchema().example(BaseResponseStatus.INVALID_USER_INFO.getMessage()))
                            .addProperty("result", null);

                    Content responseContent = new Content()
                            .addMediaType("application/json", new MediaType().schema(baseResponseSchema));

                    Content responseContentError = new Content()
                            .addMediaType("application/json", new MediaType().schema(baseResponseSchemaError));

                    ApiResponses responses = new ApiResponses()
                            .addApiResponse(String.valueOf(HttpStatus.OK.value()),
                                    new ApiResponse().description("성공").content(responseContent))
                            .addApiResponse(String.valueOf(HttpStatus.BAD_REQUEST.value()),
                                    new ApiResponse().description("잘못된 요청").content(responseContentError));

                    operation.setResponses(responses);

                    operation.addTagsItem("회원 기능");
                    operation.summary("로그인 기능");
                    operation.description("이메일과 비밀번호를 통해 로그인하고 AccessToken, RefreshToken을 발급받습니다.");

                    PathItem pathItem = new PathItem().post(operation);
                    openApi.getPaths().addPathItem("/login", pathItem);
                }
            }
        };
    }



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
