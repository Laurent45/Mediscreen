package com.openclassrooms.mediscreen.patient.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI patientOpenAPI() {
        return new OpenAPI()
                .info(
                        new Info()
                                .title("Patient Management API")
                                .description(
                                        "This REST API exposes the CRUD operations on patient model")
                                .version("1.0"));
    }
}
