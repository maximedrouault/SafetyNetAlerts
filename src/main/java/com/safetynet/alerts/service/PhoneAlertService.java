package com.safetynet.alerts.service;

import com.safetynet.alerts.dto.PersonPhoneAlertDTO;
import com.safetynet.alerts.model.DataContainer;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.utils.FireStationUtils;
import com.safetynet.alerts.utils.PersonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class PhoneAlertService {

    private final DataReader dataReader;
    private final PersonUtils personUtils;
    private final FireStationUtils fireStationUtils;


    public List<PersonPhoneAlertDTO> getPhoneAlert(int fireStationNumber) throws Exception {
        DataContainer dataContainer = dataReader.dataRead();
        List<String> coveredAddresses = fireStationUtils.getAddressesCoveredByFireStation(dataContainer.getFirestations(), fireStationNumber);
        List<Person> coveredPersons = personUtils.getCoveredPersonsByAddresses(dataContainer.getPersons(), coveredAddresses);

        if (coveredAddresses.isEmpty() || coveredPersons.isEmpty()) {
            log.error("No address or person covered by station number {}", fireStationNumber);
            return List.of();
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