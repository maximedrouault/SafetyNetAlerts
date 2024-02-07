package com.safetynet.alerts.utils;

import com.safetynet.alerts.model.Person;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PersonUtils {

    public List<Person> getCoveredPersonsByAddress(List<Person> persons, String address) {
        return persons.stream()
                .filter(person -> person.getAddress().equals(address))
                .toList();
    }

    public List<Person> getPersonsInCity(List<Person> persons, String city) {
        return persons.stream()
                .filter(person -> person.getCity().equals(city))
                .toList();
    }

    public List<Person> getPersonsByFirstNameAndLastName(List<Person> persons, String firstName, String lastName) {
        return persons.stream()
                .filter(person -> person.getFirstName().equals(firstName) && person.getLastName().equals(lastName))
                .toList();
    }

    public List<Person> getCoveredPersonsByAddresses(List<Person> persons, List<String> addresses) {
        return persons.stream()
                .filter(person -> addresses.contains(person.getAddress()))
                .toList();
    }
}