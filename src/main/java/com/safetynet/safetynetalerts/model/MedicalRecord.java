package com.safetynet.safetynetalerts.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "medicalRecords")
public class MedicalRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;
    private String birthdate; // Format à confirmer (Date) pour la manipulation future.

    @ElementCollection
    private List<String> medications;

    @ElementCollection
    private List<String> allergies;

}