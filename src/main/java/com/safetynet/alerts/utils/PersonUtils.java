package com.safetynet.alerts.utils;

import com.safetynet.alerts.model.Person;
import java.util.List;


public class PersonUtils {

    public static List<Person> findPersonsByAddress(List<Person> persons, String address) {
        return persons.stream()
                .filter(person -> person.getAddress().equals(address))
                .toList();
    }
}