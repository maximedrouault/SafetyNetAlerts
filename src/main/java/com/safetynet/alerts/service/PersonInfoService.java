package com.safetynet.alerts.service;

import com.safetynet.alerts.dto.PersonInfoDTO;
import com.safetynet.alerts.model.DataContainer;
import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.utils.MedicalRecordUtils;
import com.safetynet.alerts.utils.PersonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service
@RequiredArgsConstructor
@Slf4j
public class PersonInfoService {

    private final DataReader dataReader;
    private final MedicalRecordUtils medicalRecordUtils;
    private final PersonUtils personUtils;

    public List<PersonInfoDTO> getPersonInfo(String firstName, String lastName) throws Exception {
        DataContainer dataContainer = dataReader.dataRead();
        List<Person> filteredPersons = personUtils.getPersonsByFirstNameAndLastName(dataContainer.getPersons(), firstName, lastName);

        if (filteredPersons.isEmpty()) {
            log.error("No Person found for First name : '{}' and Last name : '{}'.", firstName, lastName);
            return List.of();
        }

        Map<Person, MedicalRecord> personToMedicalRecordMap = medicalRecordUtils.createPersonToMedicalRecordMap(filteredPersons, dataContainer.getMedicalrecords());

        List<PersonInfoDTO> personInfoDTOS = filteredPersons.stream()
            .map(person -> {
                String birthdate = personToMedicalRecordMap.get(person).getBirthdate();
                int age = medicalRecordUtils.getAge(birthdate);
                return createPersonInfoDTO(person, age, personToMedicalRecordMap.get(person));
            })
            .toList();

        log.info("Person info processed for First name : '{}' and Last name : '{}'.", firstName, lastName);
        return personInfoDTOS;
    }


    private PersonInfoDTO createPersonInfoDTO(Person person, int age, MedicalRecord medicalRecord) {
        PersonInfoDTO personInfoDTO = new PersonInfoDTO();

        personInfoDTO.setLastName(person.getLastName());
        personInfoDTO.setAddress(person.getAddress());
        personInfoDTO.setAge(age);
        personInfoDTO.setEmail(person.getEmail());
        personInfoDTO.setMedications(medicalRecord.getMedications());
        personInfoDTO.setAllergies(medicalRecord.getAllergies());

        return personInfoDTO;
    }
}