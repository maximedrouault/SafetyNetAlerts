package com.safetynet.alerts.utils;

import com.safetynet.alerts.model.Person;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PersonUtils {

    public List<Person> getCoveredPersons(List<Person> persons, String address) {
        return persons.stream()
                .filter(person -> person.getAddress().equals(address))
                .toList();
    }

    public List<Person> findPersonsByCity(List<Person> persons, String city) {
        return persons.stream()
                .filter(person -> person.getCity().equals(city))
                .toList();
    }

    public List<Person> findPersonsByFirstNameAndLastName(List<Person> persons, String firstName, String lastName) {
        return persons.stream()
                .filter(person -> person.getFirstName().equals(firstName) && person.getLastName().equals(lastName))
                .toList();
    }

    public List<Person> findPersonsByAddresses(List<Person> persons, List<String> addresses) {
        return persons.stream()
                .filter(person -> addresses.contains(person.getAddress()))
                .toList();
    }
}