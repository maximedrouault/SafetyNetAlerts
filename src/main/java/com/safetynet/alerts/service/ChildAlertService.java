package com.safetynet.alerts.service;

import com.safetynet.alerts.dto.PersonChildAlertDTO;
import com.safetynet.alerts.model.DataContainer;
import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.utils.MedicalRecordUtils;
import com.safetynet.alerts.utils.PersonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import java.util.*;

/**
 * This class handles child alerts based on address.
 */
@Service
@RequiredArgsConstructor
@Log4j2
public class ChildAlertService {

    private final DataReader dataReader;
    private final PersonUtils personUtils;
    private final MedicalRecordUtils medicalRecordUtils;

    /**
     * Retrieves a list of children residing at a given address.
     *
     * @param address The address for which to retrieve children.
     * @return A list of {@link PersonChildAlertDTO} representing children residing at the specified address.
     * @throws Exception If an error occurs while retrieving data.
     */
    public List<PersonChildAlertDTO> getChildAlert(String address) throws Exception {
        DataContainer dataContainer = dataReader.dataRead();
        List<Person> coveredPersons = personUtils.getCoveredPersonsByAddress(dataContainer.getPersons(), address);

        if (coveredPersons.isEmpty()) {
            log.error("No person found at address : '{}'.", address);
            return Collections.emptyList();
        }

        Map<Person, MedicalRecord> personToMedicalRecordMap = medicalRecordUtils.createPersonToMedicalRecordMap(coveredPersons, dataContainer.getMedicalrecords());

        List<Person> children = medicalRecordUtils.getChildren(personToMedicalRecordMap);

        List<PersonChildAlertDTO> personChildAlertDTOS = children.stream()
                .map(child -> {
                    String birthdate = personToMedicalRecordMap.get(child).getBirthdate();
                    int age = medicalRecordUtils.getAge(birthdate);
                    List<Person> familyMembers = medicalRecordUtils.getFamilyMembers(coveredPersons, child);
                    return createPersonChildAlertDTO(child, age, familyMembers);
                })
                .toList();

        log.info("Child alert processed for address : '{}'.", address);
        return personChildAlertDTOS;
    }


    private PersonChildAlertDTO createPersonChildAlertDTO(Person child, int age, List<Person> familyMembers) {

        return PersonChildAlertDTO.builder()
                .firstName(child.getFirstName())
                .lastName(child.getLastName())
                .age(age)
                .familyMembers(familyMembers)
                .build();
    }
}