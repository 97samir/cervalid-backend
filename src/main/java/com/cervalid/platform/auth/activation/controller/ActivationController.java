package com.cervalid.platform.auth.activation.controller;

import com.cervalid.platform.auth.activation.dto.ActivationRequest;
import com.cervalid.platform.auth.activation.service.ActivationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/activate")
@RequiredArgsConstructor
public class ActivationController {

    private final ActivationService activationService;

    @PostMapping
    public ResponseEntity<?> activate(@RequestBody ActivationRequest request) {

        activationService.activateAccount(request);
        return ResponseEntity.ok("Cuenta activada correctamente");
    }
}
