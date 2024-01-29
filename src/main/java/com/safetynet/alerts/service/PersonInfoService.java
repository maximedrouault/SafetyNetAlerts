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
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Slf4j
public class PersonInfoService {

    private final DataReader dataReader;

    public List<PersonInfoDTO> getPersonInfo(String firstName, String lastName) throws Exception {
        DataContainer dataContainer = dataReader.dataRead();
        List<Person> personsFiltered = PersonUtils.findPersonsByFirstNameAndLastName(dataContainer.getPersons(), firstName, lastName);

        if (personsFiltered.isEmpty()) {
            log.error("No Person found for First name : '{}' and Last name : '{}'.", firstName, lastName);
            return List.of();
        }

        List<PersonInfoDTO> personInfoDTOS = personsFiltered.stream()
            .map(person -> createPersonInfoDTO(person, dataContainer))
            .toList();

        log.info("Person info processed for First name : '{}' and Last name : '{}'.", firstName, lastName);
        return personInfoDTOS;
    }


    private PersonInfoDTO createPersonInfoDTO(Person person, DataContainer dataContainer) {
        PersonInfoDTO personInfoDTO = new PersonInfoDTO();

        personInfoDTO.setLastName(person.getLastName());
        personInfoDTO.setAddress(person.getAddress());
        personInfoDTO.setEmail(person.getEmail());

        Optional<MedicalRecord> medicalRecordForPerson = MedicalRecordUtils.findMedicalRecordForPerson(person, dataContainer.getMedicalrecords());

        medicalRecordForPerson.ifPresent(medicalRecord -> {
            personInfoDTO.setMedications(medicalRecord.getMedications());
            personInfoDTO.setAllergies(medicalRecord.getAllergies());

            int ageOfPerson = MedicalRecordUtils.findAgeByBirthdate(medicalRecord);
            personInfoDTO.setAge(ageOfPerson);
        });

        return personInfoDTO;
    }
}