package com.openclassrooms.mediscreen.patient.model;

import com.openclassrooms.mediscreen.patient.validation.GenderConstraint;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Schema(hidden = true)
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "date_of_birth")
    @Past(message = "the date must be in the past")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "the date must be in the past")
    private LocalDate dateOfBirth;

    @GenderConstraint(message = "Gender must be M(MALE) or F(FEMALE)")
    @Schema(example = "M")
    private String gender;

    private String address;

    private String phone;
}
