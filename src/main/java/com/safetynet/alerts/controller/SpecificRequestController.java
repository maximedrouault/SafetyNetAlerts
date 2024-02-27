package com.safetynet.alerts.controller;

import com.safetynet.alerts.dto.*;
import com.safetynet.alerts.service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@Tag(name = "API Controller pour les opérations de requêtes spécifiques en cas d'alerte")
@RestController
@RequiredArgsConstructor
public class SpecificRequestController {

    private final ChildAlertService childAlertService;
    private final FireAddressInfoService fireAddressInfoService;
    private final FireStationCoverageService fireStationCoverageService;
    private final PhoneAlertService phoneAlertService;
    private final CommunityEmailService communityEmailService;
    private final PersonInfoService personInfoService;
    private final FloodStationCoverageService floodStationCoverageService;


    // ChildAlert
    @GetMapping("/childAlert")
    @Operation(description = "Fournit une liste d'enfants (<=18ans) habitant à l'adresse spécifiée. " +
            "La Liste comprend le prénom, le nom et l'âge de chaque enfant, ainsi que les autres membres du foyer.")
    public ResponseEntity<List<PersonChildAlertDTO>> getChildAlert(@RequestParam @NotBlank String address) throws Exception {
        List<PersonChildAlertDTO> personChildAlertDTOS = childAlertService.getChildAlert(address);
        return (personChildAlertDTOS.isEmpty()) ? ResponseEntity.notFound().build() : ResponseEntity.ok(personChildAlertDTOS);
    }

    // FireAddressInfo
    @GetMapping("/fire")
    @Operation(description = "Fournit une liste des habitants vivant à l’adresse donnée ainsi que le numéro de la caserne de pompiers la desservant. " +
            "La liste inclue le nom, le numéro de téléphone, l'âge et les antécédents médicaux de chaque personne.")
    public ResponseEntity<FireAddressInfoResponseDTO> getFireAddressInfo(@RequestParam @NotBlank String address) throws Exception {
        FireAddressInfoResponseDTO fireAddressInfoResponseDTO = fireAddressInfoService.getFireAddressInfo(address);
        return (fireAddressInfoResponseDTO.getPersons().isEmpty()) ? ResponseEntity.notFound().build() : ResponseEntity.ok(fireAddressInfoResponseDTO);
    }

    // FireStationCoverage
    @GetMapping("/firestation")
    @Operation(description = "Fournit une liste des personnes couvertes par la caserne de pompiers correspondante. " +
            "La liste inclue le prénom, le nom, l'adresse et le numéro de téléphone de chaque personne. " +
            "Elle fournit également des compteurs du nombre total d'adultes et d'enfants (<= 18ans) dans la zone desservie.")
    public ResponseEntity<FireStationCoverageResponseDTO> getFireStationCoverage(@RequestParam @Min(1) int stationNumber) throws Exception {
        FireStationCoverageResponseDTO fireStationCoverageResponseDTO = fireStationCoverageService.getFireStationCoverage(stationNumber);
        return (fireStationCoverageResponseDTO.getPersons().isEmpty()) ? ResponseEntity.notFound().build() : ResponseEntity.ok(fireStationCoverageResponseDTO);
    }

    // PhoneAlert
    @GetMapping("/phoneAlert")
    @Operation(description = "Fournit une liste des numéros de téléphone des résidents desservis par la caserne de pompiers.")
    public ResponseEntity<List<PersonPhoneAlertDTO>> getPhoneAlert(@RequestParam(name = "firestation") @Min(1) int fireStationNumber) throws Exception {
        List<PersonPhoneAlertDTO> personPhoneAlertDTOS = phoneAlertService.getPhoneAlert(fireStationNumber);
        return (personPhoneAlertDTOS.isEmpty()) ? ResponseEntity.notFound().build() : ResponseEntity.ok(personPhoneAlertDTOS);
    }

    // CommunityEmail
    @GetMapping("/communityEmail")
    @Operation(description = "Fournit une liste des adresses emails de tous les habitants de la ville.")
    public ResponseEntity<List<PersonCommunityEmailDTO>> getCommunityEmail(@RequestParam @NotBlank String city) throws Exception {
        List<PersonCommunityEmailDTO> personCommunityEmailDTOS = communityEmailService.getCommunityEmail(city);
        return (personCommunityEmailDTOS.isEmpty()) ? ResponseEntity.notFound().build() : ResponseEntity.ok(personCommunityEmailDTOS);
    }

    // PersonInfo
    @GetMapping("/personInfo")
    @Operation(description = "Fournit une liste des informations de l'habitant spécifié. " +
            "La liste inclue le nom, l'adresse, l'âge, l'adresse email et les antécédents médicaux de chaque habitant.")
    public ResponseEntity<List<PersonInfoDTO>> getPersonInfo(@RequestParam @NotBlank String firstName, @NotBlank String lastName) throws Exception {
        List<PersonInfoDTO> personInfoDTOS = personInfoService.getPersonInfo(firstName, lastName);
        return (personInfoDTOS.isEmpty()) ? ResponseEntity.notFound().build() : ResponseEntity.ok(personInfoDTOS);
    }

    // FloodStationCoverage
    @GetMapping("/flood/stations")
    @Operation(description = "Fournit une liste de tous les foyers desservis par les casernes spécifiées. " +
            "Les habitants sont regroupés par adresse et la liste inclue le nom, le numéro de téléphone, l'âge et les antécédents médicaux de chaque habitant.")
    public ResponseEntity<List<FloodStationCoverageResponseDTO>> getFloodStationCoverage(@RequestParam(name = "stations") @NotEmpty List<@NotNull @Min(1) Integer> stationNumbers) throws Exception {
        List<FloodStationCoverageResponseDTO> floodStationCoverageResponseDTOS = floodStationCoverageService.getFloodStationCoverage(stationNumbers);
        return (floodStationCoverageResponseDTOS.isEmpty()) ? ResponseEntity.notFound().build() : ResponseEntity.ok(floodStationCoverageResponseDTOS);
    }
}