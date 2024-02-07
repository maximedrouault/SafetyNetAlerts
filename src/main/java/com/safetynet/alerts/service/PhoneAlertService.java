package com.safetynet.alerts.service;

import com.safetynet.alerts.dto.PersonPhoneAlertDTO;
import com.safetynet.alerts.model.DataContainer;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.utils.FireStationUtils;
import com.safetynet.alerts.utils.PersonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import java.util.Collections;
import java.util.List;

/**
 * Service for phone alerts.
 * This class provides methods for retrieving phone alerts based on a fire station number.
 */
@Service
@RequiredArgsConstructor
@Log4j2
public class PhoneAlertService {

    private final DataReader dataReader;
    private final PersonUtils personUtils;
    private final FireStationUtils fireStationUtils;

    /**
     * Retrieves a list of phone numbers for persons covered by a specific fire station.
     *
     * @param fireStationNumber The fire station number for which to retrieve phone alerts.
     * @return A list of {@link PersonPhoneAlertDTO} representing phone numbers of covered persons.
     * @throws Exception If an error occurs while retrieving data.
     */
    public List<PersonPhoneAlertDTO> getPhoneAlert(int fireStationNumber) throws Exception {
        DataContainer dataContainer = dataReader.dataRead();
        List<String> coveredAddresses = fireStationUtils.getAddressesCoveredByFireStation(dataContainer.getFirestations(), fireStationNumber);
        List<Person> coveredPersons = personUtils.getCoveredPersonsByAddresses(dataContainer.getPersons(), coveredAddresses);

        if (coveredAddresses.isEmpty() || coveredPersons.isEmpty()) {
            log.error("No address or person covered by station number '{}'.", fireStationNumber);
            return Collections.emptyList();
        }

        List<PersonPhoneAlertDTO> personPhoneAlertDTOS = coveredPersons.stream()
                .map(this::createPersonPhoneAlertDTO)
                .toList();

        log.info("Phone alert for station number '{}' processed.", fireStationNumber);
        return personPhoneAlertDTOS;
    }


    private PersonPhoneAlertDTO createPersonPhoneAlertDTO(Person person) {

        return PersonPhoneAlertDTO.builder()
                .phone(person.getPhone())
                .build();
    }
}