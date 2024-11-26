package com.habitlab.backend.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@PreAuthorize("denyAll()")
public class AuthController {

    @GetMapping("/login")
    @PreAuthorize("hasAuthority('READ')")
    public String login() {
        return "login";
    }

}
