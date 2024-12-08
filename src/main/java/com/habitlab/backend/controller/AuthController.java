package com.habitlab.backend.controller;

import com.habitlab.backend.dto.AuthLoginRequestDTO;
import com.habitlab.backend.dto.AuthRegisterUserRequestDTO;
import com.habitlab.backend.dto.AuthResponseDTO;
import com.habitlab.backend.service.UserDetailService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/*
* TODO document this with swagger
*
* */

@RestController
@RequestMapping("/api/auth")
@PreAuthorize("permitAll()")
public class AuthController {

    @Autowired
    private UserDetailService userDetailService;

    /*
    * TODO 1: Validate the user input
    *
    * */

    @PostMapping("/sign-in")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody @Valid AuthLoginRequestDTO request) {
        return new ResponseEntity<>(this.userDetailService.login(request), HttpStatus.OK);
    }

    @PostMapping("/sign-up")
    public ResponseEntity<AuthResponseDTO> register(@RequestBody @Valid AuthRegisterUserRequestDTO request) {
        return new ResponseEntity<>(this.userDetailService.register(request), HttpStatus.CREATED);
    }



}
