package com.safetynet.alerts.utils;

import com.safetynet.alerts.interfaces.CommonMedicalInfo;
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
                                .orElse(new MedicalRecord())));
    }

    public int getAge(String birthdate) {
        LocalDate parsedBirthdate = LocalDate.parse(birthdate, DateTimeFormatter.ofPattern("MM/dd/yyyy"));
        LocalDate currentDate = LocalDate.now();
        Period period = Period.between(parsedBirthdate, currentDate);

        return period.getYears();
    }

    public void setCommonMedicalInfo(CommonMedicalInfo commonMedicalInfo, Person person, List<MedicalRecord> medicalRecords) {
        Optional<MedicalRecord> medicalRecordForPerson = getMedicalRecordForPerson(person, medicalRecords);

        medicalRecordForPerson.ifPresent(medicalRecord -> {
            commonMedicalInfo.setMedications(medicalRecord.getMedications());
            commonMedicalInfo.setAllergies(medicalRecord.getAllergies());

            int ageOfPerson = getAge(medicalRecord.getBirthdate());
            commonMedicalInfo.setAge(ageOfPerson);
        });
    }

    public long countAdults(List<Person> persons, List<MedicalRecord> medicalRecords) {
        return persons.stream()
                .map(person -> getMedicalRecordForPerson(person, medicalRecords))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(medicalRecord -> getAge(medicalRecord.getBirthdate()))
                .filter(age -> age > 18)
                .count();
    }

    public long countChildren(List<Person> persons, List<MedicalRecord> medicalRecords) {
        return persons.stream()
                .map(person -> getMedicalRecordForPerson(person, medicalRecords))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(medicalRecord -> getAge(medicalRecord.getBirthdate()))
                .filter(age -> age <= 18)
                .count();
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
                .collect(Collectors.toList());
    }


    public List<Person> getFamilyMembers(List<Person> persons, Person child) {

        return persons.stream()
                .filter(person -> person.getAddress().equals(child.getAddress()) &&
                                person.getLastName().equals(child.getLastName()))
                .filter(person -> !person.getFirstName().equals(child.getFirstName()))
                .toList();
    }
}