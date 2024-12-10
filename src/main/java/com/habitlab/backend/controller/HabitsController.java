package com.habitlab.backend.controller;
import com.habitlab.backend.dto.HabitCreateRequestDTO;
import com.habitlab.backend.dto.HabitDTO;
import com.habitlab.backend.dto.PaginatedHabitsResponseDTO;
import com.habitlab.backend.service.IHabitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/habits")
@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
public class HabitsController {

    @Autowired
    private IHabitService habitService;

    @PostMapping("/new")
    public ResponseEntity<HabitDTO> createHabit(@RequestBody HabitCreateRequestDTO habit) {
        return ResponseEntity.status(HttpStatus.CREATED).body(habitService.saveHabit(habit, getUsername()));
    }

    @GetMapping("/")
    public ResponseEntity<PaginatedHabitsResponseDTO> getHabits(
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(habitService.getHabits(getUsername(), startDate, size));
    }

    @PutMapping("/{id}")
    public ResponseEntity<HabitDTO> updateHabit(@PathVariable String id, @RequestBody HabitDTO updatedHabit) {
        return ResponseEntity.ok(habitService.updateHabit(id, updatedHabit, getUsername()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HabitDTO> deleteHabit(@PathVariable String id) {
        return ResponseEntity.ok(habitService.deleteHabit(id, getUsername()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<HabitDTO> getHabit(@PathVariable String id) {
        return ResponseEntity.ok(habitService.getHabit(id, getUsername()));
    }


    /*
    * TODO move search implementation to service, and try to apply filters with steps.
    *
    * if( bla bla bla)
    * habits = habits applying filter
    *
    * if( bla bla bla)
    * habits = habits applying filter ...
    *
    * */

    @GetMapping("/search")
    public ResponseEntity<List<HabitDTO>> searchHabits(@RequestParam(required = false) String title,
                                                          @RequestParam(required = false) LocalDate startDateAfter,
                                                          @RequestParam(required = false) LocalDate startDateBefore) {
        if(title != null) {
            return ResponseEntity.ok(habitService.getHabitsByTitle(title, getUsername()));
        } else if(startDateAfter != null) {
            return ResponseEntity.ok(habitService.getHabitsByStartDateBetween(startDateAfter, startDateBefore!=null ? startDateBefore : LocalDate.now().plusDays(1), getUsername()));
        } else {
            return ResponseEntity.badRequest().build();
        }
    }


    private String getUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getPrincipal().toString();
    }

}
