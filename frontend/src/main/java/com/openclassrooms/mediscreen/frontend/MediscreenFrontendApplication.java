package com.openclassrooms.mediscreen.frontend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MediscreenFrontendApplication {

    public static void main(String[] args) {
        SpringApplication.run(MediscreenFrontendApplication.class, args);
    }

}
