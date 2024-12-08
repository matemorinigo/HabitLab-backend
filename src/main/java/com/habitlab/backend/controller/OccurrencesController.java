package com.habitlab.backend.controller;

import com.habitlab.backend.dto.OccurrenceViewDTO;
import com.habitlab.backend.service.OccurrenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/occurrences")
@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
public class OccurrencesController {

    @Autowired
    private OccurrenceService occurrenceService;

    @GetMapping("/{habitId}")
    public ResponseEntity<List<OccurrenceViewDTO>> getOccurrences(@PathVariable String habitId,
                                                                  @RequestParam(required = false) LocalDate dateAfter,
                                                                  @RequestParam(required = false) LocalDate dateBefore) {

        if(dateAfter != null) {
            return ResponseEntity.ok(occurrenceService.getOccurrancesBetweenDates(habitId, getUsername(),  dateAfter, dateBefore != null ? dateBefore : LocalDate.now().plusDays(1)));
        }
        return ResponseEntity.ok(occurrenceService.getOccurrencesByHabitId(habitId, getUsername()));
    }

    @PostMapping("/{habitId}")
    public ResponseEntity<OccurrenceViewDTO> createOccurrence(@PathVariable String habitId) {
        return ResponseEntity.ok(occurrenceService.createOccurrence(habitId, getUsername()));
    }


    private String getUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getPrincipal().toString();
    }
}
