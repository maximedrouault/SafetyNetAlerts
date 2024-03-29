package com.safetynet.alerts.service;

import com.safetynet.alerts.dto.FloodStationCoverageResponseDTO;
import com.safetynet.alerts.dto.PersonFloodStationCoverageDTO;
import com.safetynet.alerts.model.DataContainer;
import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.utils.FireStationUtils;
import com.safetynet.alerts.utils.MedicalRecordUtils;
import com.safetynet.alerts.utils.PersonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Service for managing flood station coverage information.
 * This class provides methods to retrieve information about persons affected by floods
 * in specified fire stations.
 */
@Service
@RequiredArgsConstructor
@Log4j2
public class FloodStationCoverageService {

    private final DataReader dataReader;
    private final MedicalRecordUtils medicalRecordUtils;
    private final PersonUtils personUtils;
    private final FireStationUtils fireStationUtils;

    /**
     * Retrieves flood station coverage information for specified station numbers.
     *
     * @param stationNumbers The list of fire station numbers for which to retrieve flood coverage information.
     * @return A list of {@link FloodStationCoverageResponseDTO} objects containing information about the flood coverage for each station.
     * @throws Exception If an error occurs while fetching the data.
     */
    public List<FloodStationCoverageResponseDTO> getFloodStationCoverage(List<Integer> stationNumbers) throws Exception {
        DataContainer dataContainer = dataReader.dataRead();
        List<String> coveredAddresses = fireStationUtils.getAddressesCoveredByFireStations(dataContainer.getFirestations(), stationNumbers);
        List<Person> coveredPersons = personUtils.getCoveredPersonsByAddresses(dataContainer.getPersons(), coveredAddresses);

        if (coveredAddresses.isEmpty() || coveredPersons.isEmpty()) {
            log.error("No Fire station or person found for station numbers : '{}'.", stationNumbers);
            return Collections.emptyList();
        }

        Map<Person, MedicalRecord> personToMedicalRecordMap = medicalRecordUtils.createPersonToMedicalRecordMap(coveredPersons, dataContainer.getMedicalrecords());

        Map<String, List<PersonFloodStationCoverageDTO>> addressToPersonsDTOsMap = coveredPersons.stream()
                .collect(Collectors.groupingBy(
                        Person::getAddress,
                        Collectors.mapping(person -> {
                            String birthdate = personToMedicalRecordMap.get(person).getBirthdate();
                            int age = medicalRecordUtils.getAge(birthdate);
                            return createPersonFloodStationCoverageDTO(person, age, personToMedicalRecordMap.get(person));
                        },
                        Collectors.toList())
                ));

        List<FloodStationCoverageResponseDTO> fireStationCoverageResponseDTOS = addressToPersonsDTOsMap.entrySet().stream()
                .map(entry -> createFloodStationCoverageResponseDTO(entry.getKey(), entry.getValue()))
                .toList();

        log.info("Flood station coverage for station numbers '{}' processed.", stationNumbers);
        return fireStationCoverageResponseDTOS;
    }


    private FloodStationCoverageResponseDTO createFloodStationCoverageResponseDTO(String address, List<PersonFloodStationCoverageDTO> persons) {

        return FloodStationCoverageResponseDTO.builder()
                .address(address)
                .persons(persons)
                .build();
    }

    private PersonFloodStationCoverageDTO createPersonFloodStationCoverageDTO(Person person, int age, MedicalRecord medicalRecord) {

        return PersonFloodStationCoverageDTO.builder()
                .lastName(person.getLastName())
                .phone(person.getPhone())
                .age(age)
                .medications(medicalRecord.getMedications())
                .allergies(medicalRecord.getAllergies())
                .build();
    }
}