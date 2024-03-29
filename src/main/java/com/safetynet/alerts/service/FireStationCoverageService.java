package com.safetynet.alerts.service;

import com.safetynet.alerts.dto.FireStationCoverageResponseDTO;
import com.safetynet.alerts.dto.PersonFireStationCoverageDTO;
import com.safetynet.alerts.model.DataContainer;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.utils.FireStationUtils;
import com.safetynet.alerts.utils.MedicalRecordUtils;
import com.safetynet.alerts.utils.PersonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import java.util.*;

/**
 * Service for managing fire station coverage information.
 * This class provides methods to retrieve information about persons and coverage
 * of a specific fire station.
 */
@Service
@RequiredArgsConstructor
@Log4j2
public class FireStationCoverageService {

    private final DataReader dataReader;
    private final MedicalRecordUtils medicalRecordUtils;
    private final PersonUtils personUtils;
    private final FireStationUtils fireStationUtils;

    /**
     * Retrieves fire station coverage information for a given station number.
     *
     * @param stationNumber The number of the fire station for which to retrieve coverage information.
     * @return A {@link FireStationCoverageResponseDTO} object containing information about the coverage of the fire station.
     * @throws Exception If an error occurs while fetching the data.
     */
    public FireStationCoverageResponseDTO getFireStationCoverage(int stationNumber) throws Exception {
        DataContainer dataContainer = dataReader.dataRead();
        List<String> coveredAddresses = fireStationUtils.getAddressesCoveredByFireStation(dataContainer.getFirestations(), stationNumber);
        List<Person> coveredPersons = personUtils.getCoveredPersonsByAddresses(dataContainer.getPersons(), coveredAddresses);

        if (coveredAddresses.isEmpty() || coveredPersons.isEmpty()) {
            log.error("No Fire station or person found for station number : '{}'.", stationNumber);
            return createFireStationCoverageResponseDTO(Collections.emptyList(), 0, 0);
        }

        int[] adultsAndChildrenCounts = medicalRecordUtils.countAdultsAndChildren(coveredPersons, dataContainer.getMedicalrecords());
        int adultsCount = adultsAndChildrenCounts[0];
        int childrenCount = adultsAndChildrenCounts[1];

        List<PersonFireStationCoverageDTO> personFireStationCoverageDTOS = coveredPersons.stream()
                .map(this::createPersonFireStationCoverageDTO)
                .toList();

        log.info("Fire station coverage for station number '{}' processed. Adults count: '{}', Children count: '{}'.", stationNumber, adultsCount, childrenCount);
        return createFireStationCoverageResponseDTO(personFireStationCoverageDTOS, adultsCount, childrenCount);
    }


    private PersonFireStationCoverageDTO createPersonFireStationCoverageDTO(Person person) {

        return PersonFireStationCoverageDTO.builder()
                .firstName(person.getFirstName())
                .lastName(person.getLastName())
                .address(person.getAddress())
                .phone(person.getPhone())
                .build();
    }

    private FireStationCoverageResponseDTO createFireStationCoverageResponseDTO(List<PersonFireStationCoverageDTO> personFireStationCoverageDTOS, int adultsCount, int childrenCount) {

        return FireStationCoverageResponseDTO.builder()
                .persons(personFireStationCoverageDTOS)
                .adultsCount(adultsCount)
                .childrenCount(childrenCount)
                .build();
    }
}