package com.habitlab.backend.controller;
import com.habitlab.backend.dto.HabitDTO;
import com.habitlab.backend.persistance.entity.HabitEntity;
import com.habitlab.backend.service.IHabitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/habits")
@PreAuthorize("permitAll()")
public class HabitsController {

    @Autowired
    private IHabitService habitService;

    @GetMapping("/")
    public ResponseEntity<List<HabitEntity>> getHabits() {
        return ResponseEntity.ok(habitService.getHabits());
    }

    @PostMapping("/new")
    public ResponseEntity<HabitEntity> createHabit(@RequestBody HabitDTO habit) {
        return ResponseEntity.status(HttpStatus.CREATED).body(habitService.saveHabit(habit));
    }

}
