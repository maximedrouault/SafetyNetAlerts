package com.safetynet.alerts.service;

import com.safetynet.alerts.model.DataContainer;
import com.safetynet.alerts.model.Person;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PersonService {

    private final DataReader dataReader;
    private final DataWriter dataWriter;

    public ResponseEntity<List<Person>> getPersons() throws Exception {
        List<Person> persons = dataReader.dataRead().getPersons();

        if (persons.isEmpty()) {
            log.error("No Persons found");
            return ResponseEntity.notFound().build();
        } else {
            log.info("Persons successfully retrieved");
            return ResponseEntity.ok(persons);
        }
    }

    public ResponseEntity<Void> deletePerson(String firstName, String lastName) throws Exception {
        DataContainer dataContainer = dataReader.dataRead();
        List<Person> persons = dataContainer.getPersons();

        for (Person person : persons) {
            if (person.getFirstName().equals(firstName) && person.getLastName().equals(lastName)) {
                persons.remove(person);

                log.info("Person successfully deleted");
                dataWriter.dataWrite(dataContainer);

                return ResponseEntity.ok().build();
            }
        }

        log.error("Person not found");
        return ResponseEntity.notFound().build();
    }

    public ResponseEntity<Person> updatePerson(Person personToUpdate) throws Exception {
        DataContainer dataContainer = dataReader.dataRead();
        List<Person> persons = dataContainer.getPersons();

        for (Person existingPerson : persons) {
            if (existingPerson.getFirstName().equals(personToUpdate.getFirstName()) &&
                    existingPerson.getLastName().equals(personToUpdate.getLastName())) {

                existingPerson.setAddress(personToUpdate.getAddress());
                existingPerson.setCity(personToUpdate.getCity());
                existingPerson.setZip(personToUpdate.getZip());
                existingPerson.setPhone(personToUpdate.getPhone());
                existingPerson.setEmail(personToUpdate.getEmail());

                log.info("Person successfully updated");
                dataWriter.dataWrite(dataContainer);

                return ResponseEntity.ok(existingPerson);
            }
        }

        log.error("Not updated, Person not found");
        return ResponseEntity.notFound().build();
    }
}