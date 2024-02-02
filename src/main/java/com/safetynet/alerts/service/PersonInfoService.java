package com.safetynet.alerts.service;

import com.safetynet.alerts.dto.PersonInfoDTO;
import com.safetynet.alerts.model.DataContainer;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.utils.MedicalRecordUtils;
import com.safetynet.alerts.utils.PersonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class PersonInfoService {

    private final DataReader dataReader;
    private final MedicalRecordUtils medicalRecordUtils;
    private final PersonUtils personUtils;

    public List<PersonInfoDTO> getPersonInfo(String firstName, String lastName) throws Exception {
        DataContainer dataContainer = dataReader.dataRead();
        List<Person> personsFiltered = personUtils.findPersonsByFirstNameAndLastName(dataContainer.getPersons(), firstName, lastName);

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

        medicalRecordUtils.setCommonMedicalInfo(personInfoDTO, person, dataContainer.getMedicalrecords());

        return personInfoDTO;
    }
}