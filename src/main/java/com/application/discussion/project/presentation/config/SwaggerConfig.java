package com.application.discussion.project.presentation.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI(){
        return new OpenAPI().info(
            new Info()
                .title("議論アプリ API")
                .version("1.0")
                .description("Spring boot RestFull API using springdoc-openapi and OpenAPI 3.0")
                .license(
                    new License().name("Apache 2.0")
                    .url("https://springdoc.org")
                )
        );
    }
}
