package com.habitlab.backend.util;

import com.habitlab.backend.exception.ResourceNotFoundException;
import com.habitlab.backend.exception.UnauthorizedException;
import com.habitlab.backend.persistance.entity.HabitEntity;
import com.habitlab.backend.repository.HabitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ValidationUtils {

    @Autowired
    HabitRepository habitRepository;

    public HabitEntity validateHabitOwner(String id, String username){
        UUID uuid = UUID.fromString(id);

        HabitEntity habit = habitRepository.findById(uuid).orElseThrow(()-> new ResourceNotFoundException("Habit not found with id: " + id));

        if(!habit.getUser().getUsername().equals(username)){
            throw new UnauthorizedException("Habit not found with id: " + habit.getId());
        }

        return habit;
    }

}
