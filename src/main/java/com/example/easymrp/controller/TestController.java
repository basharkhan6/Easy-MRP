package com.example.easymrp.controller;

import com.example.easymrp.model.auth.AuthUser;
import com.example.easymrp.service.auth.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/test")
@CrossOrigin
@RequiredArgsConstructor
public class TestController {

    private final RoleService roleService;


    @GetMapping("/all")
    public String allAccess() {
        return "Public Content.";
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('USER')")
    public String userAccess(Authentication authentication) {
        AuthUser authUser = (AuthUser) authentication.getPrincipal();
        return "User Content.";
    }

    @GetMapping("/user/role")
    @PreAuthorize("hasRole('USER')")
    public String getRoles(Authentication authentication) {
        AuthUser authUser = (AuthUser) authentication.getPrincipal();
        roleService.listAssigned(authUser.getUserModel());
        return "User Content.";
    }

}