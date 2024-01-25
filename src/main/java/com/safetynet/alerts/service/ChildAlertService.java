package com.safetynet.alerts.service;

import com.safetynet.alerts.dto.ChildInfoDTO;
import com.safetynet.alerts.model.DataContainer;
import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.model.Person;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Slf4j
public class ChildAlertService {

    private final DataReader dataReader;

    public Optional<List<ChildInfoDTO>> getChildAlert(String address) throws Exception {
        DataContainer dataContainer = dataReader.dataRead();
        List<Person> persons = dataContainer.getPersons();
        List<MedicalRecord> medicalRecords = dataContainer.getMedicalrecords();


        // Step 1 : Get a list of Persons living at the given address
        List<Person> personsAtAddress = persons.stream()
                .filter(person -> person.getAddress().equals(address))
                .toList();

        if (personsAtAddress.isEmpty()) {
            log.error("No person found at address : {}", address);
            return Optional.empty();
        }


        // Step 2: Find children based on birthdate
        List<Person> children = personsAtAddress.stream()
                .filter(person -> {
                    Optional<MedicalRecord> personMedicalRecord = medicalRecords.stream()
                            .filter(medicalRecord -> medicalRecord.getFirstName().equals(person.getFirstName()) &&
                                    medicalRecord.getLastName().equals(person.getLastName()))
                            .findFirst();

                    if (personMedicalRecord.isPresent()) {

                        LocalDate birthdate = LocalDate.parse(personMedicalRecord.get().getBirthdate(), DateTimeFormatter.ofPattern("MM/dd/yyyy"));
                        int age = Period.between(birthdate, LocalDate.now()).getYears();
                        return age <= 18;
                    } else {
                        return false;
                    }
                })
                .toList();


        // Step 3 : Return ChildInfoDTO object
        List<ChildInfoDTO> childrenInfoDTO = children.stream()
                .map(child -> {
                    ChildInfoDTO childInfoDTO = new ChildInfoDTO();

                    childInfoDTO.setFirstName(child.getFirstName());
                    childInfoDTO.setLastName(child.getLastName());

                    // Calculate age and set it in the DTO
                    Optional<MedicalRecord> childMedicalRecord = medicalRecords.stream()
                            .filter(medicalRecord ->
                                    medicalRecord.getFirstName().equals(child.getFirstName()) &&
                                    medicalRecord.getLastName().equals(child.getLastName()))
                            .findFirst();

                    if (childMedicalRecord.isPresent()) {
                        LocalDate birthdate = LocalDate.parse(childMedicalRecord.get().getBirthdate(), DateTimeFormatter.ofPattern("MM/dd/yyyy"));
                        int age = Period.between(birthdate, LocalDate.now()).getYears();

                        childInfoDTO.setAge(age);
                    }

                    // Add member of family in the DTO
                    List<Person> familyMember = persons.stream()
                            .filter(person ->
                                    !person.getFirstName().equals(child.getFirstName()) &&
                                    person.getLastName().equals(child.getLastName()) &&
                                    person.getAddress().equals(child.getAddress()))
                            .toList();

                    if (!familyMember.isEmpty()) {
                        childInfoDTO.setFamilyMembers(familyMember);
                    }

                    return childInfoDTO;
                })
                .toList();


        log.info("Child alert processed for address '{}'.", address);
        return Optional.of(childrenInfoDTO);
    }
}