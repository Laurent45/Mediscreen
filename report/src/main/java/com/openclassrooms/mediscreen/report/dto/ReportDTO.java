package com.openclassrooms.mediscreen.report.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReportDTO {
    private String firstName;
    private String lastName;
    private int age;
    private String level;
}
