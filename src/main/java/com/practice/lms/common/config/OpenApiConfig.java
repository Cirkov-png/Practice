package com.practice.lms.common.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.OAuthFlow;
import io.swagger.v3.oas.models.security.OAuthFlows;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    private static final String BASIC_AUTH = "basicAuth";
    private static final String OAUTH2 = "oauth2";
    private static final String KEYCLOAK_TOKEN_URL =
            "http://localhost:8081/realms/lms-realm/protocol/openid-connect/token";

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Learning Management System (LMS) API")
                        .version("1.1.0")
                        .description("REST API")
                        .contact(new Contact()
                                .name("Arseniy")
                                .email("support@practice.com")))
                .addSecurityItem(new SecurityRequirement().addList(BASIC_AUTH).addList(OAUTH2))
                .components(new Components()
                        .addSecuritySchemes(BASIC_AUTH, new SecurityScheme()
                                .name(BASIC_AUTH)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("basic"))
                        .addSecuritySchemes(OAUTH2, new SecurityScheme()
                                .name(OAUTH2)
                                .type(SecurityScheme.Type.OAUTH2)
                                .flows(new OAuthFlows()
                                        .password(new OAuthFlow()
                                                .tokenUrl(KEYCLOAK_TOKEN_URL)))));
    }
}