package com.safetynet.alerts.service;

import com.safetynet.alerts.dto.FloodStationCoverageResponseDTO;
import com.safetynet.alerts.dto.PersonFloodStationCoverageDTO;
import com.safetynet.alerts.model.DataContainer;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.utils.FireStationUtils;
import com.safetynet.alerts.utils.MedicalRecordUtils;
import com.safetynet.alerts.utils.PersonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class FloodStationCoverageService {

    private final DataReader dataReader;

    public List<FloodStationCoverageResponseDTO> getFloodStationCoverage(List<Integer> stationNumbers) throws Exception {
        DataContainer dataContainer = dataReader.dataRead();
        List<String> fireStationAddressForNumbers = FireStationUtils.findFireStationAddressByNumbers(dataContainer.getFirestations(), stationNumbers);
        List<Person> personsAtAddress = PersonUtils.findPersonsByAddresses(dataContainer.getPersons(), fireStationAddressForNumbers);

        if (fireStationAddressForNumbers.isEmpty() || personsAtAddress.isEmpty()) {
            log.error("No Fire station or person found for station numbers : '{}'.", stationNumbers);
            return List.of();
        }

        Map<String, List<PersonFloodStationCoverageDTO>> addressToPersonsMap = createAddressToPersonsMap(personsAtAddress, dataContainer);

        List<FloodStationCoverageResponseDTO> fireStationCoverageResponseDTOS = fireStationAddressForNumbers.stream()
                .map(address -> createFloodStationCoverageResponseDTO(address, addressToPersonsMap.get(address)))
                .toList();

        log.info("Flood station coverage for station numbers '{}' processed.", stationNumbers);
        return fireStationCoverageResponseDTOS;
    }


    private Map<String, List<PersonFloodStationCoverageDTO>> createAddressToPersonsMap(List<Person> persons, DataContainer dataContainer) {
        return persons.stream()
                .collect(Collectors.groupingBy(Person::getAddress,
                        Collectors.mapping(person -> createPersonFloodStationCoverageDTO(person, dataContainer),
                        Collectors.toList())));
    }


    private FloodStationCoverageResponseDTO createFloodStationCoverageResponseDTO(String address, List<PersonFloodStationCoverageDTO> persons) {
        FloodStationCoverageResponseDTO floodStationCoverageResponseDTO = new FloodStationCoverageResponseDTO();

        floodStationCoverageResponseDTO.setAddress(address);
        floodStationCoverageResponseDTO.setPersons(persons);

        return floodStationCoverageResponseDTO;
    }


    private PersonFloodStationCoverageDTO createPersonFloodStationCoverageDTO(Person person, DataContainer dataContainer) {
        PersonFloodStationCoverageDTO personFloodStationCoverageDTO = new PersonFloodStationCoverageDTO();

        personFloodStationCoverageDTO.setLastName(person.getLastName());
        personFloodStationCoverageDTO.setPhone(person.getPhone());

        MedicalRecordUtils.setCommonMedicalInfo(personFloodStationCoverageDTO, person, dataContainer.getMedicalrecords());

        return personFloodStationCoverageDTO;
    }
}