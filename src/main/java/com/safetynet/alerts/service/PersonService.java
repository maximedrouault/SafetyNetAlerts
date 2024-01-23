package com.safetynet.alerts.service;

import com.safetynet.alerts.model.DataContainer;
import com.safetynet.alerts.model.Person;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PersonService {

    private final DataReader dataReader;
    private final DataWriter dataWriter;

    // CRUD

    public boolean deletePerson(Person personToDelete) throws Exception {
        DataContainer dataContainer = dataReader.dataRead();
        List<Person> persons = dataContainer.getPersons();

        Optional<Person> foundPerson = persons.stream()
                .filter(existingPerson ->
                        existingPerson.getFirstName().equals(personToDelete.getFirstName()) &&
                        existingPerson.getLastName().equals(personToDelete.getLastName()))
                .findFirst();

        if (foundPerson.isPresent()) {
            persons.remove(foundPerson.get());

            dataWriter.dataWrite(dataContainer);
            log.info("Person successfully deleted");

            return true;

        } else {
            log.error("Person not found");
            return false;
        }
    }

    public Optional<Person> updatePerson(Person personToUpdate) throws Exception {
        DataContainer dataContainer = dataReader.dataRead();
        List<Person> persons = dataContainer.getPersons();

        Optional<Person> foundPerson = persons.stream()
                .filter(existingPerson ->
                        existingPerson.getFirstName().equals(personToUpdate.getFirstName()) &&
                        existingPerson.getLastName().equals(personToUpdate.getLastName()))
                .findFirst();

        if (foundPerson.isPresent()) {
            Person updatedPerson = foundPerson.get();

            updatedPerson.setAddress(personToUpdate.getAddress());
            updatedPerson.setCity(personToUpdate.getCity());
            updatedPerson.setZip(personToUpdate.getZip());
            updatedPerson.setPhone(personToUpdate.getPhone());
            updatedPerson.setEmail(personToUpdate.getEmail());

            dataWriter.dataWrite(dataContainer);
            log.info("Person successfully updated");

            return Optional.of(updatedPerson);
        } else {
            log.error("Not updated, Person not found");
            return Optional.empty();
        }
    }

    public Optional<Person> addPerson(Person personToAdd) throws Exception {
        DataContainer dataContainer = dataReader.dataRead();
        List<Person> persons = dataContainer.getPersons();

        Optional<Person> foundPerson = persons.stream()
                .filter(existingPerson ->
                        existingPerson.getFirstName().equals(personToAdd.getFirstName()) &&
                                existingPerson.getLastName().equals(personToAdd.getLastName()))
                .findFirst();

        if (foundPerson.isPresent()) {
            log.error("Not added, Person already exist");
            return Optional.empty();
        } else {
            persons.add(personToAdd);

            dataWriter.dataWrite(dataContainer);
            log.info("Person successfully added");

            return Optional.of(personToAdd);
        }
    }
}