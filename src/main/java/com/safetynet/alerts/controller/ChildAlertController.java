package com.safetynet.alerts.controller;

import com.safetynet.alerts.dto.ChildInfoDTO;
import com.safetynet.alerts.service.ChildAlertService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class ChildAlertController {

    private final ChildAlertService childAlertService;

    @GetMapping("/childAlert")
    public ResponseEntity<List<ChildInfoDTO>> getChildAlert(@RequestParam String address) throws Exception {
        Optional<List<ChildInfoDTO>> childInfoDTO = childAlertService.getChildAlert(address);
        return childInfoDTO
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}