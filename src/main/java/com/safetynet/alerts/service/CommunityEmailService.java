package com.safetynet.alerts.service;


import com.safetynet.alerts.dto.PersonCommunityEmailDTO;
import com.safetynet.alerts.model.DataContainer;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.utils.PersonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * This class provides services for retrieving community email information based on a city.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CommunityEmailService {

    private final DataReader dataReader;
    private final PersonUtils personUtils;

    /**
     * Retrieves a list of email addresses for residents of a given city.
     *
     * @param city The city for which to retrieve community email addresses.
     * @return A list of {@link PersonCommunityEmailDTO} representing email addresses of residents in the specified city.
     * @throws Exception If an error occurs while retrieving data.
     */
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