package com.safetynet.alerts.service;


import com.safetynet.alerts.dto.PersonCommunityEmailDTO;
import com.safetynet.alerts.model.DataContainer;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.utils.PersonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommunityEmailService {

    private final DataReader dataReader;
    private final PersonUtils personUtils;

    public List<PersonCommunityEmailDTO> getCommunityEmail(String city) throws Exception {
        DataContainer dataContainer = dataReader.dataRead();
        List<Person> persons = personUtils.getAllPersonsInCity(dataContainer.getPersons(), city);

        if (persons.isEmpty()) {
            log.error("No Resident found for city : '{}'.", city);
            return List.of();
        }

        List<PersonCommunityEmailDTO> personCommunityEmailDTOS = persons.stream()
                .map(this::createPersonCommunityEmailDTO)
                .toList();

        log.info("Community email info processed for city : '{}'.", city);
        return personCommunityEmailDTOS;
    }


    private PersonCommunityEmailDTO createPersonCommunityEmailDTO(Person person) {

        return PersonCommunityEmailDTO.builder()
                .email(person.getEmail())
                .build();
    }
}