package com.safetynet.alerts.utils;

import com.safetynet.alerts.model.FireStation;

import java.util.List;
import java.util.Optional;


public class FireStationUtils {

    public static Optional<Integer> findFireStationNumberByAddress(List<FireStation> fireStations, String address) {
        return fireStations.stream()
                .filter(fireStation -> fireStation.getAddress().equals(address))
                .findFirst()
                .map(FireStation::getStation);
    }

}