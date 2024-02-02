package com.safetynet.alerts.service;

import com.safetynet.alerts.dto.PersonChildAlertDTO;
import com.safetynet.alerts.model.DataContainer;
import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.utils.MedicalRecordUtils;
import com.safetynet.alerts.utils.PersonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
@RequiredArgsConstructor
@Slf4j
public class ChildAlertService {

    private final DataReader dataReader;
    private final PersonUtils personUtils;
    private final MedicalRecordUtils medicalRecordUtils;


    public List<PersonChildAlertDTO> getChildAlert(String address) throws Exception {
        DataContainer dataContainer = dataReader.dataRead();
        List<Person> coveredPersons = personUtils.getCoveredPersons(dataContainer.getPersons(), address);

        if (coveredPersons.isEmpty()) {
            log.error("No person found at address : {}", address);
            return List.of();
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

        log.info("Child alert processed for address '{}'.", address);
        return personChildAlertDTOS;
    }


    private PersonChildAlertDTO createPersonChildAlertDTO(Person child, int age, List<Person> familyMembers) {
        PersonChildAlertDTO personChildAlertDTO = new PersonChildAlertDTO();

        personChildAlertDTO.setFirstName(child.getFirstName());
        personChildAlertDTO.setLastName(child.getLastName());
        personChildAlertDTO.setAge(age);
        personChildAlertDTO.setFamilyMembers(familyMembers);

        return personChildAlertDTO;
    }
}