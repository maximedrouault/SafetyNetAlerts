package com.safetynet.alerts.service;

import com.safetynet.alerts.dto.PersonChildAlertDTO;
import com.safetynet.alerts.model.DataContainer;
import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.model.Person;
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
public class ChildAlertService {

    private final DataReader dataReader;

    public List<PersonChildAlertDTO> getChildAlert(String address) throws Exception {
        DataContainer dataContainer = dataReader.dataRead();
        List<Person> personsAtAddress = PersonUtils.findPersonsByAddress(dataContainer.getPersons(), address);

        if (personsAtAddress.isEmpty()) {
            log.error("No person found at address : {}", address);
            return List.of();
        }

        List<Person> children = MedicalRecordUtils.findChildren(personsAtAddress, dataContainer.getMedicalrecords());

        List<PersonChildAlertDTO> personChildAlertDTOS = children.stream()
                .map(person -> createPersonChildAlertDTO(person, dataContainer.getMedicalrecords(), personsAtAddress))
                .toList();

        log.info("Child alert processed for address '{}'.", address);
        return personChildAlertDTOS;
    }


    private PersonChildAlertDTO createPersonChildAlertDTO(Person person, List<MedicalRecord> medicalrecords, List<Person> persons) {
        PersonChildAlertDTO personChildAlertDTO = new PersonChildAlertDTO();

        personChildAlertDTO.setFirstName(person.getFirstName());
        personChildAlertDTO.setLastName(person.getLastName());

        Optional<MedicalRecord> medicalRecordForPerson = MedicalRecordUtils.findMedicalRecordForPerson(person, medicalrecords);
        medicalRecordForPerson.ifPresent(medicalRecord -> {
            int ageForPerson = MedicalRecordUtils.findAgeByBirthdate(medicalRecord);
            personChildAlertDTO.setAge(ageForPerson);
        });

        List<Person> familyMembers = MedicalRecordUtils.findFamilyMembers(persons, person);
        personChildAlertDTO.setFamilyMembers(familyMembers);

        return personChildAlertDTO;
    }
}