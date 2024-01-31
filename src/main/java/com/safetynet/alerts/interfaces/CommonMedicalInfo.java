package com.safetynet.alerts.interfaces;

import java.util.List;

public interface CommonMedicalInfo {

    void setMedications(List<String> medications);
    void setAllergies(List<String> allergies);
    void setAge(int age);

}