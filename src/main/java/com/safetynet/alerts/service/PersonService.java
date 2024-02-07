package com.safetynet.alerts.service;

import com.safetynet.alerts.model.DataContainer;
import com.safetynet.alerts.model.Person;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

/**
 * Service for managing persons.
 * This class provides methods for performing CRUD operations on persons.
 */
@Service
@RequiredArgsConstructor
@Log4j2
public class PersonService {

    private final DataReader dataReader;
    private final DataWriter dataWriter;

    // CRUD

    /**
     * Deletes a person.
     *
     * @param personToDelete The person to delete.
     * @return True if the person was successfully deleted, false otherwise.
     * @throws Exception If an error occurs while deleting the person.
     */
    public boolean deletePerson(Person personToDelete) throws Exception {
        DataContainer dataContainer = dataReader.dataRead();
        List<Person> persons = dataContainer.getPersons();

        Optional<Person> foundPerson = persons.stream()
                .filter(existingPerson ->
                        existingPerson.getFirstName().equals(personToDelete.getFirstName()) &&
                        existingPerson.getLastName().equals(personToDelete.getLastName()))
                .findFirst();

        if (foundPerson.isPresent()) {
            Person personRemoved = foundPerson.get();
            persons.remove(personRemoved);

            dataWriter.dataWrite(dataContainer);
            log.info("Person successfully deleted for person with Firstname: '{}' and Lastname: '{}'.", personRemoved.getFirstName(), personRemoved.getLastName());

            return true;

        } else {
            log.error("Person not found for person with Firstname: '{}' and Lastname: '{}'.", personToDelete.getFirstName(), personToDelete.getLastName());
            return false;
        }
    }

    /**
     * Updates a person.
     *
     * @param personToUpdate The person to update.
     * @return An Optional containing the updated person if found, or empty if not found.
     * @throws Exception If an error occurs while updating the person.
     */
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
            log.info("Person successfully updated for person with Firstname: '{}' and Lastname: '{}'.", updatedPerson.getFirstName(), updatedPerson.getLastName());

            return Optional.of(updatedPerson);
        } else {
            log.error("Not updated, Person not found for person with Firstname: '{}' and Lastname: '{}'.", personToUpdate.getFirstName(), personToUpdate.getLastName());
            return Optional.empty();
        }
    }

    /**
     * Adds a new person.
     *
     * @param personToAdd The person to add.
     * @return An Optional containing the added person if it doesn't already exist, or empty if it already exists.
     * @throws Exception If an error occurs while adding the person.
     */
    public Optional<Person> addPerson(Person personToAdd) throws Exception {
        DataContainer dataContainer = dataReader.dataRead();
        List<Person> persons = dataContainer.getPersons();

        Optional<Person> foundPerson = persons.stream()
                .filter(existingPerson ->
                        existingPerson.getFirstName().equals(personToAdd.getFirstName()) &&
                                existingPerson.getLastName().equals(personToAdd.getLastName()))
                .findFirst();

        if (foundPerson.isPresent()) {
            log.error("Not added, Person already exist for person with Firstname: '{}' and Lastname: '{}'.", personToAdd.getFirstName(), personToAdd.getLastName());
            return Optional.empty();
        } else {
            persons.add(personToAdd);

            dataWriter.dataWrite(dataContainer);
            log.info("Person successfully added for person with Firstname: '{}' and Lastname: '{}'.", personToAdd.getFirstName(), personToAdd.getLastName());

            return Optional.of(personToAdd);
        }
    }
}