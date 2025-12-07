package com.openclassrooms.mediscreen.report;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MediscreenReportApplication {

    public static void main(String[] args) {
        SpringApplication.run(MediscreenReportApplication.class, args);
    }
}
