package com.safetynet.alerts.utils;

import com.safetynet.alerts.model.Person;
import java.util.List;


public class PersonUtils {

    public static List<Person> findPersonsByAddress(List<Person> persons, String address) {
        return persons.stream()
                .filter(person -> person.getAddress().equals(address))
                .toList();
    }

    public static List<Person> findPersonsByCity(List<Person> persons, String city) {
        return persons.stream()
                .filter(person -> person.getCity().equals(city))
                .toList();
    }

    public static List<Person> findPersonsByFirstNameAndLastName(List<Person> persons, String firstName, String lastName) {
        return persons.stream()
                .filter(person -> person.getFirstName().equals(firstName) && person.getLastName().equals(lastName))
                .toList();
    }
}