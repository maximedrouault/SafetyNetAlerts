package com.safetynet.alerts.utils;

import com.safetynet.alerts.interfaces.CommonMedicalInfo;
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

    public static void setCommonMedicalInfo(CommonMedicalInfo commonMedicalInfo, Person person, List<MedicalRecord> medicalRecords) {
        Optional<MedicalRecord> medicalRecordForPerson = findMedicalRecordForPerson(person, medicalRecords);

        medicalRecordForPerson.ifPresent(medicalRecord -> {
            commonMedicalInfo.setMedications(medicalRecord.getMedications());
            commonMedicalInfo.setAllergies(medicalRecord.getAllergies());

            int ageOfPerson = findAgeByBirthdate(medicalRecord);
            commonMedicalInfo.setAge(ageOfPerson);
        });
    }
}