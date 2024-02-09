package com.safetynet.alerts.utils;

import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.model.Person;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;


@Component
public class MedicalRecordUtils {

    public Optional<MedicalRecord> getMedicalRecordForPerson(Person person, List<MedicalRecord> medicalRecords) {

        return medicalRecords.stream()
                .filter(medicalRecord ->
                        medicalRecord.getFirstName().equals(person.getFirstName()) &&
                        medicalRecord.getLastName().equals(person.getLastName()))
                .findFirst();
    }

    public Map<Person, MedicalRecord> createPersonToMedicalRecordMap(List<Person> persons, List<MedicalRecord> medicalRecords) {

        return persons.stream()
                .collect(Collectors.toMap(
                        person -> person,
                        person -> medicalRecords.stream()
                                .filter(medicalRecord ->
                                        person.getFirstName().equals(medicalRecord.getFirstName()) &&
                                        person.getLastName().equals(medicalRecord.getLastName()))
                                .findFirst()
                                .orElse(MedicalRecord.builder().build())));
    }

    public int getAge(String birthdate) {
        LocalDate parsedBirthdate = LocalDate.parse(birthdate, DateTimeFormatter.ofPattern("MM/dd/yyyy"));
        LocalDate currentDate = LocalDate.now();
        Period period = Period.between(parsedBirthdate, currentDate);

        return period.getYears();
    }

    public int[] countAdultsAndChildren(List<Person> persons, List<MedicalRecord> medicalRecords) {
        int[] counts = new int[2]; // [adultCount, childCount]

        persons.forEach(person -> {
            Optional<MedicalRecord> medicalRecordOpt = getMedicalRecordForPerson(person, medicalRecords);
            medicalRecordOpt.ifPresent(medicalRecord -> {
                int age = getAge(medicalRecord.getBirthdate());
                if (age > 18) {
                    counts[0]++; // Increase adultCount
                } else {
                    counts[1]++; // Increase childCount
                }
            });
        });

        return counts;
    }

    public List<Person> getChildren(Map<Person, MedicalRecord> personToMedicalRecordMap) {
        LocalDate currentDate = LocalDate.now();
        int currentYear = currentDate.getYear();

        return personToMedicalRecordMap.entrySet().stream()
                .filter(entry -> {
                    LocalDate birthdate = LocalDate.parse(entry.getValue().getBirthdate(), DateTimeFormatter.ofPattern("MM/dd/yyyy"));
                    int age = currentYear - birthdate.getYear();
                    return age <= 18;
                })
                .map(Map.Entry::getKey)
                .toList();
    }

    public List<Person> getFamilyMembers(List<Person> persons, Person child) {

        return persons.stream()
                .filter(person -> person.getAddress().equals(child.getAddress()) &&
                                person.getLastName().equals(child.getLastName()))
                .filter(person -> !person.getFirstName().equals(child.getFirstName()))
                .toList();
    }
}