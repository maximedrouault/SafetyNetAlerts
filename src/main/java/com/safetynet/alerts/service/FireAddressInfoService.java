package com.safetynet.alerts.service;

import com.safetynet.alerts.dto.PersonFireAddressInfoDTO;
import com.safetynet.alerts.model.DataContainer;
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
            log.error("No Person or Fire station number found for address : '{}'.", address);
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

        MedicalRecordUtils.setCommonMedicalInfo(personFireAddressInfoDTO, person, dataContainer.getMedicalrecords());

        return personFireAddressInfoDTO;
    }
}