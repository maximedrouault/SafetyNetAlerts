package com.safetynet.alerts.service;

import com.safetynet.alerts.dto.PersonPhoneAlertDTO;
import com.safetynet.alerts.model.DataContainer;
import com.safetynet.alerts.model.FireStation;
import com.safetynet.alerts.model.Person;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
public class PhoneAlertService {

    private final DataReader dataReader;

    public Optional<List<PersonPhoneAlertDTO>> getPhoneAlert(int fireStationNumber) throws Exception {
        DataContainer dataContainer = dataReader.dataRead();
        List<FireStation> fireStations = dataContainer.getFirestations();
        List<Person> persons = dataContainer.getPersons();


        // Step 1 : Obtain a list of addresses covered based on Fire Station
        Set<String> coveredAddresses = fireStations.stream()
                .filter(fireStation -> fireStation.getStation() == fireStationNumber)
                .map(FireStation::getAddress)
                .collect(Collectors.toSet());

        if (coveredAddresses.isEmpty()) {
            log.error("No address covered by station number {}", fireStationNumber);
            return Optional.empty();
        }


        // Step 2 : Obtain a list of Persons covered based on "coveredAddresses"
        List<Person> coveredPersons = persons.stream()
                .filter(person -> coveredAddresses.contains(person.getAddress()))
                .toList();

        if (coveredPersons.isEmpty()) {
            log.error("No person covered at these addresses for station number {}", fireStationNumber);
            return Optional.empty();
        }


        // Step 5 : Return PersonPhoneAlertDTO object
        List<PersonPhoneAlertDTO> personsPhoneAlertDTO = coveredPersons.stream()
                .map(person -> {
                    PersonPhoneAlertDTO personPhoneAlertDTO = new PersonPhoneAlertDTO();

                    personPhoneAlertDTO.setPhone(person.getPhone());

                    return personPhoneAlertDTO;
                })
                .toList();

        log.info("Phone alert for station number '{}' processed.", fireStationNumber);
        return Optional.of(personsPhoneAlertDTO);
    }
}