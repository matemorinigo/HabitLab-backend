package com.habitlab.backend.controller;

import com.habitlab.backend.dto.SummaryResponseDTO;
import com.habitlab.backend.service.StatService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("api/stats")
@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
public class StatsController {

    @Autowired
    private StatService statService;

    @GetMapping("/summary")
    public ResponseEntity<SummaryResponseDTO> getSummary(@RequestParam(required = false) LocalDate dateAfter,
                                                         @RequestParam(required = false) LocalDate dateBefore) {
        return ResponseEntity.ok(statService.getSummary(getUsername(), dateAfter, dateBefore));
    }

    private String getUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getPrincipal().toString();
    }
}
