package com.halo.core_bridge.config;

import com.halo.core_bridge.api.token.filter.LoginFilter;
import com.halo.core_bridge.common.model.BaseResponseStatus;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.headers.Header;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.media.*;
import io.swagger.v3.oas.models.parameters.RequestBody;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.SecurityFilterChain;

import java.util.List;
import java.util.Optional;

@SecurityScheme(
        name = "로그인한 사용자의 AccessToken Cookie",
        type = SecuritySchemeType.APIKEY,
        in = SecuritySchemeIn.COOKIE,
        paramName = "USER_AT"
)
@SecurityScheme(
        name = "로그인한 사용자의 Refresh Cookie",
        type = SecuritySchemeType.APIKEY,
        in = SecuritySchemeIn.COOKIE,
        paramName = "USER_RT"
)
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
                            .addProperty("email", new StringSchema().example("test@corebridge.com"))
                            .addProperty("password", new StringSchema().example("qwer1234"));
                    RequestBody requestBody = new RequestBody().content(
                            new Content().addMediaType("application/json", new MediaType().schema(requestSchema))
                    );
                    operation.setRequestBody(requestBody);

                    // BaseResponse 구조 반영한 응답 스키마

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
                                    new ApiResponse()
                                            .description("성공")
                                            .content(responseContent)
                                            .addHeaderObject("Set-Cookie", new Header()
                                                    .description("AccessToken(USER_AT)과 RefreshToken(USER_RT)이 쿠키로 발급됩니다.")
                                                    .schema(new StringSchema().example("USER_AT=...; HttpOnly; Path=/\nUSER_RT=...; HttpOnly; Path=/"))))
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
        Info info = new Info()
                .title("Core Bridge API Docs")
                .version("v1.0.0")
                .description("""
                        Halo Core Bridge 프로젝트의 REST API 문서입니다.<br><br>
                        🔗 <b>API 목차</b><br>
                        • <a href="#/채용 공고 프로세스">채용 공고 프로세스</a><br>
                        • <a href="#/알림">알림</a><br>
                        • <a href="#/회원 정보 찾기">회원 정보 찾기</a><br>
                        • <a href="#/사내 공지사항">사내 공지사항</a><br>
                        • <a href="#/회원 기능">회원 기능</a><br>
                        • <a href="#/채용 공고 스케줄">채용 공고 스케줄</a><br>
                        • <a href="#/인증">인증</a><br>
                        • <a href="#/단계별 지원자 관리">단계별 지원자 관리</a><br>
                        • <a href="#/면접">면접</a><br>
                        • <a href="#/채용 공고">채용 공고</a><br>
                        • <a href="#/PDF">PDF</a><br>
                        • <a href="#/이미지">이미지</a><br>
                        • <a href="#/회원 관리">회원 관리</a><br>
                        • <a href="#/이력서">이력서</a><br>
                        • <a href="#/공개 채용 공고">공개 채용 공고</a><br>
                        • <a href="#/부서">부서</a><br>
                        """)
                .contact(
                        new Contact()
                                .name("HALO Dev Team")
                                .email("com.corebridge@gmail.com")
                                .url("https://www.core-bridge.com")
                )
                .license(
                        new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0.html")
                );

        List<Tag> tags = List.of(
                new Tag().name("인증").description("이메일 인증 관련 API"),
                new Tag().name("회원 정보 찾기").description("사용자의 이메일과 비밀번호를 찾는 API"),
                new Tag().name("회원 관리").description("회원 조회, 수정, 삭제, 등록 API"),
                new Tag().name("회원 기능").description("로그인 기능"),
                new Tag().name("사내 공지사항").description("공지사항 등록, 조회, 수정, 삭제, 전체 조회 API"),
                new Tag().name("채용 공고").description("채용공고 등록, 조회, 상세조회 관련 API"),
                new Tag().name("공개 채용 공고").description("공개용 채용 공고 목록 조회 API"),
                new Tag().name("채용 공고 프로세스").description("특정 채용 공고의 채용 단계 관련 API"),
                new Tag().name("채용 공고 스케줄").description("채용 공고 스케줄 관련 API"),
                new Tag().name("단계별 지원자 관리").description("단계별 지원자 조회, 수정 API"),
                new Tag().name("이력서").description("이력서 및 자기소개서 관련 API"),
                new Tag().name("면접").description("면접 등록 API"),
                new Tag().name("부서").description("부서 조회 API"),
                new Tag().name("이미지").description("이미지 업로드, 조회, 삭제 API"),
                new Tag().name("PDF").description("PDF 업로드, 조회, 삭제, 다운로드 API"),
                new Tag().name("알림").description("SSE를 이용한 실시간 알림 API")
        );

        return new OpenAPI()
                .info(info)
                .servers(List.of(
                        new Server().url("https://api.core-bridge.co.kr").description("Production Server"),
                        new Server().url("http://localhost:8080").description("Local Server")
                ))
                .externalDocs(new ExternalDocumentation()
                        .description("Core-Bridge 백엔드 GitHub Repository")
                        .url("https://github.com/beyond-sw-camp/be17-fin-Halo-CoreBridge-BE")
                )
                .tags(tags);
    }
}
