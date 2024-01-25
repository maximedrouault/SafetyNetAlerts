package com.safetynet.alerts.service;

import com.safetynet.alerts.dto.PersonInfoDTO;
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

    public Optional<List<PersonInfoDTO>> getFireStationCoverage(int stationNumber) throws Exception {
        DataContainer dataContainer = dataReader.dataRead();
        List<FireStation> fireStations = dataContainer.getFirestations();
        List<Person> persons = dataContainer.getPersons();
        List<MedicalRecord> medicalRecords = dataContainer.getMedicalrecords();


        // Step 1 : Obtain a list of addresses covered based on Fire Station
        Set<String> coveredAddresses = fireStations.stream()
                .filter(fireStation -> fireStation.getStation() == stationNumber)
                .map(FireStation::getAddress)
                .collect(Collectors.toSet());

        if (coveredAddresses.isEmpty()) {
            log.error("No addresses covered by station number {}", stationNumber);
            return Optional.empty();
        }


        // Step 2 : Obtain a list of Persons covered based on "coveredAddresses"
        List<Person> coveredPersons = persons.stream()
                .filter(person -> coveredAddresses.contains(person.getAddress()))
                .toList();

        if (coveredPersons.isEmpty()) {
            log.error("No persons covered at these addresses for station number {}", stationNumber);
            return Optional.empty();
        }


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


        // Step 5 : Return PersonInfoDTO object
        List<PersonInfoDTO> personsInfoDTO = coveredPersons.stream()
                .map(person -> {
                    PersonInfoDTO personInfoDTO = new PersonInfoDTO();

                    personInfoDTO.setFirstName(person.getFirstName());
                    personInfoDTO.setLastName(person.getLastName());
                    personInfoDTO.setAddress(person.getAddress());
                    personInfoDTO.setPhone(person.getPhone());
                    personInfoDTO.setAdultsCount((int) adultsCount);
                    personInfoDTO.setChildrenCount((int) childrenCount);

                    return personInfoDTO;
                })
                .toList();

        log.info("Fire station coverage for station number '{}' processed. Adults count: {}, Children count: {}", stationNumber, adultsCount, childrenCount);
        return Optional.of(personsInfoDTO);
    }
}