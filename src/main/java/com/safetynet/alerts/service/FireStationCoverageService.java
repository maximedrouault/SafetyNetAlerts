package com.safetynet.alerts.service;

import com.safetynet.alerts.dto.FireStationCoverageResponseDTO;
import com.safetynet.alerts.dto.PersonFireStationCoverageDTO;
import com.safetynet.alerts.model.DataContainer;
import com.safetynet.alerts.model.FireStation;
import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.model.Person;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
public class FireStationCoverageService {

    private final DataReader dataReader;

    public FireStationCoverageResponseDTO getFireStationCoverage(int stationNumber) throws Exception {
        DataContainer dataContainer = dataReader.dataRead();
        List<FireStation> fireStations = dataContainer.getFirestations();
        List<Person> persons = dataContainer.getPersons();
        List<MedicalRecord> medicalRecords = dataContainer.getMedicalrecords();


        // Step 1 : Obtain a list of addresses covered based on Fire Station
        Set<String> coveredAddresses = fireStations.stream()
                .filter(fireStation -> fireStation.getStation() == stationNumber)
                .map(FireStation::getAddress)
                .collect(Collectors.toSet());


        // Step 2 : Obtain a list of Persons covered based on "coveredAddresses"
        List<Person> coveredPersons = persons.stream()
                .filter(person -> coveredAddresses.contains(person.getAddress()))
                .toList();


        // Step 3 : Obtain a list of MedicalRecord covered to access birthdate
        List<MedicalRecord> coveredMedicalRecords = medicalRecords.stream()
                .filter(medicalRecord -> coveredPersons.stream()
                        .anyMatch(person -> person.getFirstName().equals(medicalRecord.getFirstName()) &&
                                person.getLastName().equals(medicalRecord.getLastName())))
                .toList();


        // Step 4 : Extraction of dates of birth to calculate ages
        List<Integer> ages = coveredMedicalRecords.stream()
                .map(medicalRecord -> {
                    LocalDate birthdate = LocalDate.parse(medicalRecord.getBirthdate(), DateTimeFormatter.ofPattern("MM/dd/yyyy"));
                    return Period.between(birthdate, LocalDate.now()).getYears();
                })
                .toList();


        // Step 5 : Count number of Adults and Children based on ages extracted
        long childrenCount = ages.stream()
                        .filter(age -> age <= 18)
                        .count();

        long adultsCount = ages.size() - childrenCount;


        // Step 6 : Create PersonFireStationCoverageDTO object
        List<PersonFireStationCoverageDTO> personsFireStationCoverageDTO = coveredPersons.stream()
                .map(person -> {
                    PersonFireStationCoverageDTO personFireStationCoverageDTO = new PersonFireStationCoverageDTO();

                    personFireStationCoverageDTO.setFirstName(person.getFirstName());
                    personFireStationCoverageDTO.setLastName(person.getLastName());
                    personFireStationCoverageDTO.setAddress(person.getAddress());
                    personFireStationCoverageDTO.setPhone(person.getPhone());

                    return personFireStationCoverageDTO;
                })
                .toList();

        // Create the DTO response object with nested PersonFireStationCoverageDTO
        FireStationCoverageResponseDTO fireStationsCoverageResponseDTO = new FireStationCoverageResponseDTO();
        fireStationsCoverageResponseDTO.setPersons(personsFireStationCoverageDTO);
        fireStationsCoverageResponseDTO.setAdultsCount((int) adultsCount);
        fireStationsCoverageResponseDTO.setChildrenCount((int) childrenCount);


        if (fireStationsCoverageResponseDTO.getPersons().isEmpty()) {
            log.error("No address or person covered by station number {}", stationNumber);
            return fireStationsCoverageResponseDTO;
        }

        log.info("Fire station coverage for station number '{}' processed. Adults count: {}, Children count: {}", stationNumber, adultsCount, childrenCount);
        return fireStationsCoverageResponseDTO;
    }
}