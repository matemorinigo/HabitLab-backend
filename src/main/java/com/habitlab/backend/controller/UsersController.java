package com.habitlab.backend.controller;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.habitlab.backend.dto.UserProfileDTO;
import com.habitlab.backend.service.UserDetailService;
import com.habitlab.backend.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/user")
@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
public class UsersController {

    @Autowired
    private UserDetailService userDetailService;

    @GetMapping("/profile")
    public ResponseEntity<UserProfileDTO> getProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String username = authentication.getPrincipal().toString();

        return ResponseEntity.ok(userDetailService.getUserProfile(username));
    }
}
