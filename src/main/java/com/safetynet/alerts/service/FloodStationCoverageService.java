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
    private final MedicalRecordUtils medicalRecordUtils;
    private final PersonUtils personUtils;
    private final FireStationUtils fireStationUtils;


    public List<FloodStationCoverageResponseDTO> getFloodStationCoverage(List<Integer> stationNumbers) throws Exception {
        DataContainer dataContainer = dataReader.dataRead();
        List<String> coveredAddresses = fireStationUtils.getAddressesCoveredByFireStations(dataContainer.getFirestations(), stationNumbers);
        List<Person> coveredPersons = personUtils.getCoveredPersonsByAddresses(dataContainer.getPersons(), coveredAddresses);

        if (coveredAddresses.isEmpty() || coveredPersons.isEmpty()) {
            log.error("No Fire station or person found for station numbers : '{}'.", stationNumbers);
            return List.of();
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
        FloodStationCoverageResponseDTO floodStationCoverageResponseDTO = new FloodStationCoverageResponseDTO();

        floodStationCoverageResponseDTO.setAddress(address);
        floodStationCoverageResponseDTO.setPersons(persons);

        return floodStationCoverageResponseDTO;
    }


    private PersonFloodStationCoverageDTO createPersonFloodStationCoverageDTO(Person person, int age, MedicalRecord medicalRecord) {
        PersonFloodStationCoverageDTO personFloodStationCoverageDTO = new PersonFloodStationCoverageDTO();

        personFloodStationCoverageDTO.setLastName(person.getLastName());
        personFloodStationCoverageDTO.setPhone(person.getPhone());
        personFloodStationCoverageDTO.setAge(age);
        personFloodStationCoverageDTO.setMedications(medicalRecord.getMedications());
        personFloodStationCoverageDTO.setAllergies(medicalRecord.getAllergies());

        return personFloodStationCoverageDTO;
    }
}