package com.safetynet.alerts.service;

import com.safetynet.alerts.dto.FireAddressInfoResponseDTO;
import com.safetynet.alerts.dto.PersonFireAddressInfoDTO;
import com.safetynet.alerts.model.DataContainer;
import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.utils.FireStationUtils;
import com.safetynet.alerts.utils.MedicalRecordUtils;
import com.safetynet.alerts.utils.PersonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Service for managing information related to an address in case of fire.
 * This class provides methods to retrieve information about people
 * covered by an address in case of fire.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class FireAddressInfoService {

    private final DataReader dataReader;
    private final PersonUtils personUtils;
    private final MedicalRecordUtils medicalRecordUtils;
    private final FireStationUtils fireStationUtils;

    /**
     * Retrieves information related to an address in case of fire.
     *
     * @param address The address for which we want to retrieve information.
     * @return A {@link FireAddressInfoResponseDTO} object containing information about the address in case of fire.
     * @throws Exception If an error occurs while fetching the data.
     */
    public FireAddressInfoResponseDTO getFireAddressInfo(String address) throws Exception {
        DataContainer dataContainer = dataReader.dataRead();
        List<Person> coveredPersons = personUtils.getCoveredPersonsByAddress(dataContainer.getPersons(), address);
        Optional<Integer> fireStationNumber = fireStationUtils.getFireStationNumberByAddress(dataContainer.getFirestations(), address);

        if (coveredPersons.isEmpty() || fireStationNumber.isEmpty()) {
            log.error("No Person or Fire station number found for address : '{}'.", address);
            return createFireAddressInfoResponseDTO(0, Collections.emptyList());
        }

        Map<Person, MedicalRecord> personToMedicalRecordMap = medicalRecordUtils.createPersonToMedicalRecordMap(coveredPersons, dataContainer.getMedicalrecords());

        List<PersonFireAddressInfoDTO> personFireAddressInfoDTOS = coveredPersons.stream()
                .map(person -> {
                    String birthdate = personToMedicalRecordMap.get(person).getBirthdate();
                    int age = medicalRecordUtils.getAge(birthdate);
                    return createPersonFireAddressInfoDTO(person, age, personToMedicalRecordMap.get(person));
                })
                .toList();

        log.info("Fire address info processed for address : '{}'.", address);
        return createFireAddressInfoResponseDTO(fireStationNumber.get(), personFireAddressInfoDTOS);
    }


    private PersonFireAddressInfoDTO createPersonFireAddressInfoDTO(Person person, int age, MedicalRecord medicalRecord) {

        return PersonFireAddressInfoDTO.builder()
                .lastName(person.getLastName())
                .phone(person.getPhone())
                .medications(medicalRecord.getMedications())
                .allergies(medicalRecord.getAllergies())
                .age(age)
                .build();
    }

    private FireAddressInfoResponseDTO createFireAddressInfoResponseDTO(int fireStationNumber, List<PersonFireAddressInfoDTO> personFireAddressInfoDTOS) {

        return FireAddressInfoResponseDTO.builder()
                .stationNumber(fireStationNumber)
                .persons(personFireAddressInfoDTOS)
                .build();
    }
}