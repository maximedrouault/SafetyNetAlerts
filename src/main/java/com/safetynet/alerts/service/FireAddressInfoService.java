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
    private final PersonUtils personUtils;
    private final MedicalRecordUtils medicalRecordUtils;
    private final FireStationUtils fireStationUtils;


    public List<PersonFireAddressInfoDTO> getFireAddressInfo(String address) throws Exception {
        DataContainer dataContainer = dataReader.dataRead();
        List<Person> personsAtAddress = personUtils.getCoveredPersonsByAddress(dataContainer.getPersons(), address);
        Optional<Integer> fireStationNumberForAddress = fireStationUtils.findFireStationNumberByAddress(dataContainer.getFirestations(), address);

        if (personsAtAddress.isEmpty() || fireStationNumberForAddress.isEmpty()) {
            log.error("No Person or Fire station number found for address : '{}'.", address);
            return List.of();
        }

        List<PersonFireAddressInfoDTO> personFireAddressInfoDTOS = personsAtAddress.stream()
            .map(person -> createPersonFireAddressInfoDTO(person, fireStationNumberForAddress, dataContainer.getMedicalrecords()))
            .toList();

        log.info("Fire address info processed for address : '{}'.", address);
        return personFireAddressInfoDTOS;
    }


    private PersonFireAddressInfoDTO createPersonFireAddressInfoDTO(Person person, Optional<Integer> fireStationNumberForAddress, List<MedicalRecord> medicalRecords) {
        PersonFireAddressInfoDTO personFireAddressInfoDTO = new  PersonFireAddressInfoDTO();

        personFireAddressInfoDTO.setLastName(person.getLastName());
        personFireAddressInfoDTO.setPhone(person.getPhone());
        fireStationNumberForAddress.ifPresent(personFireAddressInfoDTO::setStationNumber);

        medicalRecordUtils.setCommonMedicalInfo(personFireAddressInfoDTO, person, medicalRecords);

        return personFireAddressInfoDTO;
    }
}