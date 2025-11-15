package com.openclassrooms.mediscreen_report.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI reportOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Diabetes Assessment Report API")
                        .description("This REST API exposes a report about diabetes assessment")
                        .version("1.0"));
    }
}
