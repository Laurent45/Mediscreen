package com.openclassrooms.mediscreen.note.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI noteOpenAPI() {
        return new OpenAPI()
                .info(
                        new Info()
                                .title("Note Management API")
                                .description(
                                        "This REST API exposes the CRUD operations on note model")
                                .version("1.0"));
    }
}
