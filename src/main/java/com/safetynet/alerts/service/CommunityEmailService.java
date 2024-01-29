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

    public List<PersonCommunityEmailDTO> getCommunityEmail(String city) throws Exception {
        DataContainer dataContainer = dataReader.dataRead();
        List<Person> personsAtCity = PersonUtils.findPersonsByCity(dataContainer.getPersons(), city);

        if (personsAtCity.isEmpty()) {
            log.error("No Person found for city : '{}'.", city);
            return List.of();
        }

        List<PersonCommunityEmailDTO> personCommunityEmailDTOS = personsAtCity.stream()
                .map(this::createPersonCommunityEmailDTO)
                .toList();

        log.info("Community email info processed for city : '{}'.", city);
        return personCommunityEmailDTOS;
    }


    private PersonCommunityEmailDTO createPersonCommunityEmailDTO(Person person) {
        PersonCommunityEmailDTO personCommunityEmailDTO = new PersonCommunityEmailDTO();

        personCommunityEmailDTO.setEmail(person.getEmail());

        return personCommunityEmailDTO;
    }
}