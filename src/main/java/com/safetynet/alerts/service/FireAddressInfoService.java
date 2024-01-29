package com.safetynet.alerts.service;

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
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Slf4j
public class FireAddressInfoService {

    private final DataReader dataReader;

    public List<PersonFireAddressInfoDTO> getFireAddressInfo(String address) throws Exception {
        DataContainer dataContainer = dataReader.dataRead();
        List<Person> personsAtAddress = PersonUtils.findPersonsByAddress(dataContainer.getPersons(), address);
        Optional<Integer> fireStationNumberForAddress = FireStationUtils.findFireStationNumberByAddress(dataContainer.getFirestations(), address);

        if (personsAtAddress.isEmpty() || fireStationNumberForAddress.isEmpty()) {
            log.error("No Persons or Fire station number found for address : '{}'.", address);
            return List.of();
        }

        List<PersonFireAddressInfoDTO> personFireAddressInfoDTOS = personsAtAddress.stream()
            .map(person -> createPersonFireAddressInfoDTO(person, fireStationNumberForAddress, dataContainer))
            .toList();

        log.info("Fire address info processed for address : '{}'.", address);
        return personFireAddressInfoDTOS;
    }


    private PersonFireAddressInfoDTO createPersonFireAddressInfoDTO(Person person, Optional<Integer> fireStationNumberForAddress, DataContainer dataContainer) {
        PersonFireAddressInfoDTO personFireAddressInfoDTO = new  PersonFireAddressInfoDTO();

        personFireAddressInfoDTO.setLastName(person.getLastName());
        personFireAddressInfoDTO.setPhone(person.getPhone());
        fireStationNumberForAddress.ifPresent(personFireAddressInfoDTO::setStationNumber);

        Optional<MedicalRecord> medicalRecordForPerson = MedicalRecordUtils.findMedicalRecordForPerson(person, dataContainer.getMedicalrecords());

        medicalRecordForPerson.ifPresent(medicalRecord -> {
            personFireAddressInfoDTO.setMedications(medicalRecord.getMedications());
            personFireAddressInfoDTO.setAllergies(medicalRecord.getAllergies());

            int ageOfPerson = MedicalRecordUtils.findAgeByBirthdate(medicalRecord);
            personFireAddressInfoDTO.setAge(ageOfPerson);
        });

        return personFireAddressInfoDTO;
    }
}