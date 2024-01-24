package com.safetynet.alerts.service;

import com.safetynet.alerts.dto.PersonInfoDTO;
import com.safetynet.alerts.model.DataContainer;
import com.safetynet.alerts.model.FireStation;
import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.model.Person;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.time.Year;
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


        // Step 3 : Obtain a list of years of birthdate of covered Persons
        Map<String, MedicalRecord> nameToMedicalRecordMap = medicalRecords.stream()
                .collect(Collectors.toMap(
                        medicalRecord -> medicalRecord.getFirstName() + " " + medicalRecord.getLastName(),
                        medicalRecord -> medicalRecord));

        List<MedicalRecord> coveredMedicalRecords = coveredPersons.stream()
                .map(person -> nameToMedicalRecordMap.get(person.getFirstName() + " " + person.getLastName()))
                .filter(Objects::nonNull)
                .toList();

        List<Integer> yearsOfBirth = coveredMedicalRecords.stream()
                .map(medicalRecord -> {
                    String birthdate = medicalRecord.getBirthdate();
                    return Integer.parseInt(birthdate.substring(birthdate.length() - 4));
                })
                .toList();


        // Step 4 : Count number of Adults and Children
        int currentYear = Year.now().getValue();

        long childrenCount = yearsOfBirth.stream()
                        .filter(yearOfBirth -> (currentYear - yearOfBirth) <= 18)
                        .count();

        long adultsCount = yearsOfBirth.size() - childrenCount;


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

        log.info("Fire station coverage for station number {} processed. Adults count: {}, Children count: {}", stationNumber, adultsCount, childrenCount);
        return Optional.of(personsInfoDTO);
    }
}