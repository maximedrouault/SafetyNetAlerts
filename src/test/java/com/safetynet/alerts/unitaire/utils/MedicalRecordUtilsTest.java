package com.safetynet.alerts.unitaire.utils;

import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.utils.MedicalRecordUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class MedicalRecordUtilsTest {

    private static MedicalRecordUtils medicalRecordUtils;

    @BeforeAll
    public static void setUp() {
        medicalRecordUtils = new MedicalRecordUtils();
    }


    // getMedicalRecordForPerson
    @Test
    public void getMedicalRecordForPersonWithMatchingMedicalRecord() {
        // Test with right Firstname and Lastname to test the first way of matching
        Person person = Person.builder().firstName("John").lastName("Boyd").build();
        List<MedicalRecord> medicalRecords = new ArrayList<>();
        medicalRecords.add(MedicalRecord.builder().firstName("John").lastName("Boyd").build());

        Optional<MedicalRecord> medicalRecord = medicalRecordUtils.getMedicalRecordForPerson(person, medicalRecords);

        assertTrue(medicalRecord.isPresent());
        assertEquals("John", medicalRecord.get().getFirstName());
        assertEquals("Boyd", medicalRecord.get().getLastName());
    }

    @Test
    public void getMedicalRecordForPersonWhenPersonExistsButNoMatchingMedicalRecord() {
        // Test with another LastName to test the other way of matching
        Person person = Person.builder().firstName("John").lastName("Shepard").build();
        List<MedicalRecord> medicalRecords = new ArrayList<>();
        medicalRecords.add(MedicalRecord.builder().firstName("John").lastName("Boyd").build());

        Optional<MedicalRecord> medicalRecord = medicalRecordUtils.getMedicalRecordForPerson(person, medicalRecords);

        assertTrue(medicalRecord.isEmpty());
    }

    @Test
    public void getMedicalRecordForPersonWhenMedicalRecordNotFound() {
        Person person = Person.builder().firstName("Peter").lastName("Duncan").build();
        List<MedicalRecord> medicalRecords = new ArrayList<>();
        medicalRecords.add(MedicalRecord.builder().firstName("John").lastName("Boyd").build());

        Optional<MedicalRecord> medicalRecord = medicalRecordUtils.getMedicalRecordForPerson(person, medicalRecords);

        assertTrue(medicalRecord.isEmpty());
    }

    @Test
    public void getMedicalRecordForPersonWhenPersonIsEmpty() {
        Person person = Person.builder().build();
        List<MedicalRecord> medicalRecords = new ArrayList<>();
        medicalRecords.add(MedicalRecord.builder().firstName("John").lastName("Boyd").build());

        Optional<MedicalRecord> medicalRecord = medicalRecordUtils.getMedicalRecordForPerson(person, medicalRecords);

        assertTrue(medicalRecord.isEmpty());
    }

    @Test
    public void getMedicalRecordForPersonWhenMedicalRecordsIsEmpty() {
        Person person = Person.builder().firstName("Peter").lastName("Duncan").build();
        List<MedicalRecord> medicalRecords = new ArrayList<>();

        Optional<MedicalRecord> medicalRecord = medicalRecordUtils.getMedicalRecordForPerson(person, medicalRecords);

        assertTrue(medicalRecord.isEmpty());
    }


    //createPersonToMedicalRecordMap
    @Test
    public void createPersonToMedicalRecordMapWithMatchingPersonAndMedicalRecord() {
        List<Person> persons = new ArrayList<>();
        Person person1 = Person.builder().firstName("John").lastName("Boyd").build();
        persons.add(person1);
        List<MedicalRecord> medicalRecords = new ArrayList<>();
        MedicalRecord medicalRecord1 = MedicalRecord.builder().firstName("John").lastName("Boyd").build();
        medicalRecords.add(medicalRecord1);

        Map<Person, MedicalRecord> personToMedicalRecordMap = medicalRecordUtils.createPersonToMedicalRecordMap(persons, medicalRecords);

        assertEquals(1, personToMedicalRecordMap.size());
        assertTrue(personToMedicalRecordMap.containsKey(person1));
        assertEquals(medicalRecord1, personToMedicalRecordMap.get(person1));
    }

    @Test
    public void createPersonToMedicalRecordMapWithEmptyPersonsList() {
        List<Person> persons = new ArrayList<>();
        List<MedicalRecord> medicalRecords = new ArrayList<>();
        medicalRecords.add(MedicalRecord.builder().firstName("John").lastName("Boyd").build());

        Map<Person, MedicalRecord> personToMedicalRecordMap = medicalRecordUtils.createPersonToMedicalRecordMap(persons, medicalRecords);

        assertTrue(personToMedicalRecordMap.isEmpty());
    }

    @Test
    public void createPersonToMedicalRecordMapWithEmptyMedicalRecordsList() {
        List<Person> persons = new ArrayList<>();
        Person person1 = Person.builder().firstName("John").lastName("Boyd").build();
        persons.add(person1);
        List<MedicalRecord> medicalRecords = new ArrayList<>();

        Map<Person, MedicalRecord> personToMedicalRecordMap = medicalRecordUtils.createPersonToMedicalRecordMap(persons, medicalRecords);
        MedicalRecord associatedMedicalRecord = personToMedicalRecordMap.get(person1);

        assertEquals(persons.size(), personToMedicalRecordMap.size());
        assertTrue(personToMedicalRecordMap.containsKey(person1));
        assertNotNull(associatedMedicalRecord);
        assertNull(associatedMedicalRecord.getFirstName());
        assertNull(associatedMedicalRecord.getLastName());
    }

    @Test
    public void createPersonToMedicalRecordMapWithNonMatchingPersonFirstName() {
        List<Person> persons = new ArrayList<>();
        Person person1 = Person.builder().firstName("John").lastName("Boyd").build();
        persons.add(person1);
        List<MedicalRecord> medicalRecords = new ArrayList<>();
        medicalRecords.add(MedicalRecord.builder().firstName("Foster").lastName("Boyd").build());

        Map<Person, MedicalRecord> personToMedicalRecordMap = medicalRecordUtils.createPersonToMedicalRecordMap(persons, medicalRecords);
        MedicalRecord associatedMedicalRecord = personToMedicalRecordMap.get(person1);

        assertEquals(persons.size(), personToMedicalRecordMap.size());
        assertTrue(personToMedicalRecordMap.containsKey(person1));
        assertNotNull(associatedMedicalRecord);
        assertNull(associatedMedicalRecord.getFirstName());
        assertNull(associatedMedicalRecord.getLastName());
    }

    @Test
    public void createPersonToMedicalRecordMapWithNonMatchingPersonLastName() {
        List<Person> persons = new ArrayList<>();
        Person person1 = Person.builder().firstName("John").lastName("Boyd").build();
        persons.add(person1);
        List<MedicalRecord> medicalRecords = new ArrayList<>();
        medicalRecords.add(MedicalRecord.builder().firstName("John").lastName("Shepard").build());

        Map<Person, MedicalRecord> personToMedicalRecordMap = medicalRecordUtils.createPersonToMedicalRecordMap(persons, medicalRecords);
        MedicalRecord associatedMedicalRecord = personToMedicalRecordMap.get(person1);

        assertEquals(persons.size(), personToMedicalRecordMap.size());
        assertTrue(personToMedicalRecordMap.containsKey(person1));
        assertNotNull(associatedMedicalRecord);
        assertNull(associatedMedicalRecord.getFirstName());
        assertNull(associatedMedicalRecord.getLastName());
    }


    // getAge
    @Test
    public void getAgeReturnsCorrectAge() {
        int expectedAge = 24;
        LocalDate currentDate = LocalDate.now();
        LocalDate birthdate = currentDate.minusYears(expectedAge); // Pre Calculation of the simulated date of birth to make the test immutable over time

        int calculatedAge = medicalRecordUtils.getAge(birthdate.format(DateTimeFormatter.ofPattern("MM/dd/yyyy")));

        assertEquals(expectedAge, calculatedAge);
    }

    @Test
    public void getAgeWithFutureBirthdate() {
        LocalDate currentDate = LocalDate.now();
        LocalDate futureBirthdate = currentDate.plusYears(1);

        int calculatedAge = medicalRecordUtils.getAge(futureBirthdate.format(DateTimeFormatter.ofPattern("MM/dd/yyyy")));

        assertEquals(-1, calculatedAge);
    }

    @Test
    public void getAgeWithNullBirthdate() {
        assertThrows(NullPointerException.class, () -> medicalRecordUtils.getAge(null));
    }

    @Test
    public void getAgeWithEmptyBirthdate() {
        assertThrows(DateTimeParseException.class, () -> medicalRecordUtils.getAge(""));
    }

    @Test
    public void testGetAgeWithInvalidFormatBirthdate() {
        assertThrows(DateTimeParseException.class, () -> medicalRecordUtils.getAge("2000/01/01"));
    }


    // countAdultsAndChildren
    @Test
    public void countAdultsAndChildrenWithMixedAdultsAndChildren() {
        List<Person> persons = new ArrayList<>();
        persons.add(Person.builder().firstName("John").lastName("Boyd").build());
        persons.add(Person.builder().firstName("Foster").lastName("Shepard").build());
        List<MedicalRecord> medicalRecords = new ArrayList<>();
        medicalRecords.add(MedicalRecord.builder().firstName("John").lastName("Boyd").birthdate("01/01/2000").build());
        // Create a simulated year of birthdate to make the test immutable
        String simulatedYearOfBirtDate = LocalDate.now().minusYears(10).format(DateTimeFormatter.ofPattern("yyyy"));
        medicalRecords.add(MedicalRecord.builder().firstName("Foster").lastName("Shepard").birthdate("01/01/" + simulatedYearOfBirtDate).build());

        int[] counts = medicalRecordUtils.countAdultsAndChildren(persons, medicalRecords);

        assertEquals(1, counts[0]); // 1 adult
        assertEquals(1, counts[1]); // 1 child
    }

    @Test
    public void countAdultsAndChildrenWithAllAdults() {
        List<Person> persons = new ArrayList<>();
        persons.add(Person.builder().firstName("John").lastName("Boyd").build());
        persons.add(Person.builder().firstName("Foster").lastName("Shepard").build());
        List<MedicalRecord> medicalRecords = new ArrayList<>();
        medicalRecords.add(MedicalRecord.builder().firstName("John").lastName("Boyd").birthdate("01/01/2000").build());
        medicalRecords.add(MedicalRecord.builder().firstName("Foster").lastName("Shepard").birthdate("01/01/2002").build());

        int[] counts = medicalRecordUtils.countAdultsAndChildren(persons, medicalRecords);

        assertEquals(2, counts[0]); // 2 adult
        assertEquals(0, counts[1]); // 0 child
    }

    @Test
    public void countAdultsAndChildrenWithAllChildren() {
        List<Person> persons = new ArrayList<>();
        persons.add(Person.builder().firstName("John").lastName("Boyd").build());
        persons.add(Person.builder().firstName("Foster").lastName("Shepard").build());
        List<MedicalRecord> medicalRecords = new ArrayList<>();
        // Create a simulated year of birthdate to make the test immutable
        String simulatedYearOfBirtDate1 = LocalDate.now().minusYears(10).format(DateTimeFormatter.ofPattern("yyyy"));
        medicalRecords.add(MedicalRecord.builder().firstName("John").lastName("Boyd").birthdate("01/01/" + simulatedYearOfBirtDate1).build());
        String simulatedYearOfBirtDate2 = LocalDate.now().minusYears(8).format(DateTimeFormatter.ofPattern("yyyy"));
        medicalRecords.add(MedicalRecord.builder().firstName("Foster").lastName("Shepard").birthdate("01/01/" + simulatedYearOfBirtDate2).build());

        int[] counts = medicalRecordUtils.countAdultsAndChildren(persons, medicalRecords);

        assertEquals(0, counts[0]); // 0 adult
        assertEquals(2, counts[1]); // 2 child
    }

    @Test
    public void countAdultsAndChildrenWithEmptyPersonList() {
        List<Person> persons = new ArrayList<>();
        List<MedicalRecord> medicalRecords = new ArrayList<>();
        medicalRecords.add(MedicalRecord.builder().firstName("John").lastName("Boyd").birthdate("01/01/2000").build());

        int[] counts = medicalRecordUtils.countAdultsAndChildren(persons, medicalRecords);

        assertEquals(0, counts[0]); // 0 adult
        assertEquals(0, counts[1]); // 0 child
    }

    @Test
    public void countAdultsAndChildrenWithEmptyMedicalRecords() {
        List<Person> persons = new ArrayList<>();
        persons.add(Person.builder().firstName("John").lastName("Boyd").build());
        List<MedicalRecord> medicalRecords = new ArrayList<>();

        int[] counts = medicalRecordUtils.countAdultsAndChildren(persons, medicalRecords);

        assertEquals(0, counts[0]); // 0 adult
        assertEquals(0, counts[1]); // 0 child
    }


    // getChildren
    @Test
    public void getChildrenWithChildren() {
        List<Person> persons = new ArrayList<>();
        persons.add(Person.builder().firstName("John").lastName("Boyd").build());
        persons.add(Person.builder().firstName("Foster").lastName("Shepard").build());
        List<MedicalRecord> medicalRecords = new ArrayList<>();
        medicalRecords.add(MedicalRecord.builder().firstName("John").lastName("Boyd").birthdate("01/01/2000").build());
        // Create a simulated year of birthdate to make the test immutable
        String simulatedYearOfBirtDate = LocalDate.now().minusYears(10).format(DateTimeFormatter.ofPattern("yyyy"));
        medicalRecords.add(MedicalRecord.builder().firstName("Foster").lastName("Shepard").birthdate("01/01/" + simulatedYearOfBirtDate).build());
        Map<Person, MedicalRecord> personToMedicalRecordMap = new HashMap<>();
        personToMedicalRecordMap.put(persons.get(0), medicalRecords.get(0));
        personToMedicalRecordMap.put(persons.get(1), medicalRecords.get(1));

        List<Person> children = medicalRecordUtils.getChildren(personToMedicalRecordMap);

        assertEquals(1, children.size());
        assertEquals("Foster", children.get(0).getFirstName());
        assertEquals("Shepard", children.get(0).getLastName());
    }

    @Test
    public void getChildrenWithoutChildren() {
        List<Person> persons = new ArrayList<>();
        persons.add(Person.builder().firstName("John").lastName("Boyd").build());
        List<MedicalRecord> medicalRecords = new ArrayList<>();
        medicalRecords.add(MedicalRecord.builder().firstName("John").lastName("Boyd").birthdate("01/01/2000").build());
        Map<Person, MedicalRecord> personToMedicalRecordMap = new HashMap<>();
        personToMedicalRecordMap.put(persons.get(0), medicalRecords.get(0));

        List<Person> children = medicalRecordUtils.getChildren(personToMedicalRecordMap);

        assertEquals(0, children.size());
    }

    @Test
    public void getChildrenWithEmptyMap() {
        Map<Person, MedicalRecord> personToMedicalRecordMap = new HashMap<>();

        List<Person> children = medicalRecordUtils.getChildren(personToMedicalRecordMap);

        assertEquals(0, children.size());
    }


    // getFamilyMembers
    @Test
    public void getFamilyMembersWithWithMatchingLastNameAndAddress() {
        List<Person> persons = new ArrayList<>();
        persons.add(Person.builder().firstName("John").lastName("Boyd").address("1509 Culver St").build());
        persons.add(Person.builder().firstName("Foster").lastName("Shepard").address("748 Townings Dr").build());
        Person child = Person.builder().firstName("Jacob").lastName("Boyd").address("1509 Culver St").build();

        List<Person> familyMembers = medicalRecordUtils.getFamilyMembers(persons, child);

        assertEquals(1, familyMembers.size());
        assertEquals("John", familyMembers.get(0).getFirstName());
        assertEquals("Boyd", familyMembers.get(0).getLastName());
        assertEquals("1509 Culver St", familyMembers.get(0).getAddress());
    }

    @Test
    public void getFamilyMembersWithMatchingAddressButNotLastName() {
        List<Person> persons = new ArrayList<>();
        persons.add(Person.builder().firstName("John").lastName("Duncan").address("1509 Culver St").build());
        persons.add(Person.builder().firstName("Foster").lastName("Shepard").address("748 Townings Dr").build());
        Person child = Person.builder().firstName("Jacob").lastName("Boyd").address("1509 Culver St").build();

        List<Person> familyMembers = medicalRecordUtils.getFamilyMembers(persons, child);

        assertEquals(0, familyMembers.size());
    }

    @Test
    public void getFamilyMembersWithMatchingLastNameButNotAddress() {
        List<Person> persons = new ArrayList<>();
        persons.add(Person.builder().firstName("John").lastName("Boyd").address("1509 Culver St").build());
        persons.add(Person.builder().firstName("Foster").lastName("Shepard").address("748 Townings Dr").build());
        Person child = Person.builder().firstName("Jacob").lastName("Boyd").address("748 Townings Dr").build();

        List<Person> familyMembers = medicalRecordUtils.getFamilyMembers(persons, child);

        assertEquals(0, familyMembers.size());
    }
}