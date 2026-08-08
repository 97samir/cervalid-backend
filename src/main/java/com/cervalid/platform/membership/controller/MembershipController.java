package com.cervalid.platform.membership.controller;

import com.cervalid.platform.membership.dto.CreateMembershipRequest;
import com.cervalid.platform.membership.dto.MembershipResponse;
import com.cervalid.platform.membership.service.InstitutionMembershipService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

// crea relacion de un usuario con la institucion, si este usuario no tiene la relacion creada
@RestController
@RequestMapping("/membership")
@RequiredArgsConstructor
public class MembershipController {

    private final InstitutionMembershipService membershipService;

    @PostMapping
    public MembershipResponse create(
            @RequestBody CreateMembershipRequest request) {
        return membershipService.createMembership(request);
    }
}