package com.openclassrooms.mediscreen_note.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI noteOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Note Management API")
                        .description("This REST API exposes the CRUD operations on note model")
                        .version("1.0"));
    }
}
