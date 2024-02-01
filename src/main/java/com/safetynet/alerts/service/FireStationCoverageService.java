package com.safetynet.alerts.service;

import com.safetynet.alerts.dto.FireStationCoverageResponseDTO;
import com.safetynet.alerts.dto.PersonFireStationCoverageDTO;
import com.safetynet.alerts.model.DataContainer;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.utils.FireStationUtils;
import com.safetynet.alerts.utils.MedicalRecordUtils;
import com.safetynet.alerts.utils.PersonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.*;


@Service
@RequiredArgsConstructor
@Slf4j
public class FireStationCoverageService {

    private final DataReader dataReader;

    public FireStationCoverageResponseDTO getFireStationCoverage(int stationNumber) throws Exception {
        DataContainer dataContainer = dataReader.dataRead();
        List<String> fireStationAddressForNumber = FireStationUtils.findFireStationAddressByNumber(dataContainer.getFirestations(), stationNumber);
        List<Person> personsAtAddress = PersonUtils.findPersonsByAddresses(dataContainer.getPersons(), fireStationAddressForNumber);

        if (fireStationAddressForNumber.isEmpty() || personsAtAddress.isEmpty()) {
            log.error("No Fire station or person found for station number : '{}'.", stationNumber);
            return createFireStationCoverageResponseDTO(Collections.emptyList(), 0, 0);
        }

        long adultsCount = MedicalRecordUtils.countAdults(personsAtAddress, dataContainer.getMedicalrecords());
        long childrenCount = MedicalRecordUtils.countChildren(personsAtAddress, dataContainer.getMedicalrecords());

        List<PersonFireStationCoverageDTO> personFireStationCoverageDTOS = personsAtAddress.stream()
                .map(this::createPersonFireStationCoverageDTO)
                .toList();

        log.info("Fire station coverage for station number '{}' processed. Adults count: {}, Children count: {}", stationNumber, adultsCount, childrenCount);
        return createFireStationCoverageResponseDTO(personFireStationCoverageDTOS, adultsCount, childrenCount);
    }


    private PersonFireStationCoverageDTO createPersonFireStationCoverageDTO(Person person) {
        PersonFireStationCoverageDTO personFireStationCoverageDTO = new PersonFireStationCoverageDTO();

        personFireStationCoverageDTO.setFirstName(person.getFirstName());
        personFireStationCoverageDTO.setLastName(person.getLastName());
        personFireStationCoverageDTO.setAddress(person.getAddress());
        personFireStationCoverageDTO.setPhone(person.getPhone());

        return personFireStationCoverageDTO;
    }


    private FireStationCoverageResponseDTO createFireStationCoverageResponseDTO(List<PersonFireStationCoverageDTO> persons, long adultsCount, long childrenCount) {
        FireStationCoverageResponseDTO fireStationCoverageResponseDTO = new FireStationCoverageResponseDTO();

        fireStationCoverageResponseDTO.setPersons(persons);
        fireStationCoverageResponseDTO.setAdultsCount((int) adultsCount);
        fireStationCoverageResponseDTO.setChildrenCount((int) childrenCount);

        return fireStationCoverageResponseDTO;
    }
}