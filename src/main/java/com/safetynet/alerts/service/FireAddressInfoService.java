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


@Service
@RequiredArgsConstructor
@Slf4j
public class FireAddressInfoService {

    private final DataReader dataReader;
    private final PersonUtils personUtils;
    private final MedicalRecordUtils medicalRecordUtils;
    private final FireStationUtils fireStationUtils;


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
        PersonFireAddressInfoDTO personFireAddressInfoDTO = new  PersonFireAddressInfoDTO();

        personFireAddressInfoDTO.setLastName(person.getLastName());
        personFireAddressInfoDTO.setPhone(person.getPhone());
        personFireAddressInfoDTO.setMedications(medicalRecord.getMedications());
        personFireAddressInfoDTO.setAllergies(medicalRecord.getAllergies());
        personFireAddressInfoDTO.setAge(age);

        return personFireAddressInfoDTO;
    }


    private FireAddressInfoResponseDTO createFireAddressInfoResponseDTO(int fireStationNumber, List<PersonFireAddressInfoDTO> personFireAddressInfoDTOS) {
        FireAddressInfoResponseDTO fireAddressInfoResponseDTO = new FireAddressInfoResponseDTO();

        fireAddressInfoResponseDTO.setStationNumber(fireStationNumber);
        fireAddressInfoResponseDTO.setPersons(personFireAddressInfoDTOS);

        return fireAddressInfoResponseDTO;
    }
}