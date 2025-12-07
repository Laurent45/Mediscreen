package com.openclassrooms.mediscreen.frontend.proxy;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.openclassrooms.mediscreen.frontend.dto.ReportDTO;

@FeignClient(name = "reportApi", url = "http://report:8080/api/v1/reportPatient/")
public interface ReportProxy {

    @GetMapping("/")
    ReportDTO getReport(@RequestParam("patientId") Long patientId);
}
