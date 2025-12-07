package com.openclassrooms.mediscreen.patient.model;

import java.time.LocalDate;
import java.util.Objects;

import jakarta.persistence.*;
import jakarta.validation.constraints.Past;

import org.springframework.format.annotation.DateTimeFormat;

import com.openclassrooms.mediscreen.patient.validation.GenderConstraint;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Patient patient)) return false;
        return Objects.equals(id, patient.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
