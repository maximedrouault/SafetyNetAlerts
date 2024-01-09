package com.safetynet.safetynetalerts.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "fireStations")
public class FireStation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String address;
    private int station;

}