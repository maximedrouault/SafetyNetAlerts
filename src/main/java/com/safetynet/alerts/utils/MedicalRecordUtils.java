package com.safetynet.alerts.utils;

import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.model.Person;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;


public class MedicalRecordUtils {

    public static Optional<MedicalRecord> findMedicalRecordForPerson(Person person, List<MedicalRecord> medicalRecords) {
        return medicalRecords.stream()
                .filter(medicalRecord ->
                        medicalRecord.getFirstName().equals(person.getFirstName()) &&
                        medicalRecord.getLastName().equals(person.getLastName()))
                .findFirst();
    }

    public static int findAgeByBirthdate(MedicalRecord medicalRecord) {
        LocalDate birthdate = LocalDate.parse(medicalRecord.getBirthdate(), DateTimeFormatter.ofPattern("MM/dd/yyyy"));
        LocalDate currentDate = LocalDate.now();
        Period period = Period.between(birthdate, currentDate);

        return period.getYears();
    }
}