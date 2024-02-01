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

    public static long countAdults(List<Person> persons, List<MedicalRecord> medicalRecords) {
        List<Integer> ages = persons.stream()
                .map(person -> MedicalRecordUtils.findMedicalRecordForPerson(person, medicalRecords))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(MedicalRecordUtils::findAgeByBirthdate)
                .toList();

        return ages.stream()
                .filter(age -> age > 18)
                .count();
    }

    public static long countChildren(List<Person> persons, List<MedicalRecord> medicalRecords) {
        List<Integer> ages = persons.stream()
                .map(person -> MedicalRecordUtils.findMedicalRecordForPerson(person, medicalRecords))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(MedicalRecordUtils::findAgeByBirthdate)
                .toList();

        return ages.stream()
                .filter(age -> age <= 18)
                .count();
    }

    public static List<Person> findChildren(List<Person> persons, List<MedicalRecord> medicalRecords) {

        return persons.stream()
                .filter(person -> MedicalRecordUtils.findMedicalRecordForPerson(person, medicalRecords)
                        .map(medicalRecord -> MedicalRecordUtils.findAgeByBirthdate(medicalRecord) <= 18)
                        .orElse(false))
                .toList();
    }

    public static List<Person> findFamilyMembers(List<Person> persons, Person child) {

        return persons.stream()
                .filter(person -> person.getAddress().equals(child.getAddress()) &&
                                person.getLastName().equals(child.getLastName()))
                .filter(person -> !person.getFirstName().equals(child.getFirstName()))
                .toList();
    }
}