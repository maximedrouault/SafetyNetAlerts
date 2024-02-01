package com.safetynet.alerts.utils;

import com.safetynet.alerts.model.FireStation;

import java.util.List;
import java.util.Optional;


public class FireStationUtils {

    public static Optional<Integer> findFireStationNumberByAddress(List<FireStation> fireStations, String address) {
        return fireStations.stream()
                .filter(fireStation -> fireStation.getAddress().equals(address))
                .map(FireStation::getStation)
                .findFirst();
    }

    public static List<String> findFireStationAddressByNumbers(List<FireStation> fireStations, List<Integer> stationNumbers) {
        return fireStations.stream()
                .filter(fireStation -> stationNumbers.contains(fireStation.getStation()))
                .map(FireStation::getAddress)
                .toList();
    }

    public static List<String> findFireStationAddressByNumber(List<FireStation> fireStations, int stationNumber) {
        return fireStations.stream()
                .filter(fireStation -> stationNumber == fireStation.getStation())
                .map(FireStation::getAddress)
                .toList();
    }
}