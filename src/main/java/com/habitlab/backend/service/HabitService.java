package com.habitlab.backend.service;

import com.habitlab.backend.dto.HabitCreateRequestDTO;
import com.habitlab.backend.dto.HabitDTO;
import com.habitlab.backend.exception.ResourceNotFoundException;
import com.habitlab.backend.persistance.entity.HabitEntity;
import com.habitlab.backend.persistance.entity.UserEntity;
import com.habitlab.backend.repository.HabitRepository;
import com.habitlab.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service
public class HabitService implements IHabitService {

    @Autowired
    private HabitRepository habitRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<HabitEntity> getHabits() {
        List<HabitEntity> habits = habitRepository.findAll();

        return habits;
    }

    @Override
    public HabitEntity getHabit(String id) {
        HabitEntity habit = habitRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Habit not found with id: " + id));
        return habit;
    }

    @Override
    public HabitEntity getHabitByTitle(String title) {
        HabitEntity habit = habitRepository.findByTitle(title).orElseThrow(()-> new ResourceNotFoundException("Habit not found with title: " + title));
        return habit;
    }

    @Override
    public HabitEntity updateHabit(String id, HabitDTO updatedHabit) {
        HabitEntity habit = habitRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Habit not found with id: " + id));
        if(updatedHabit.getTitle() != null) {
            habit.setTitle(updatedHabit.getTitle());
        }
        if(updatedHabit.getDescription() != null) {
            habit.setDescription(updatedHabit.getDescription());
        }
        if(updatedHabit.getStartDate() != null){
            habit.setStartDate(updatedHabit.getStartDate());
        }
        if(updatedHabit.getEndDate() != null){
            habit.setEndDate(updatedHabit.getEndDate());
        }

        return habitRepository.save(habit);
    }

    @Override
    public HabitDTO saveHabit(HabitCreateRequestDTO habit, String username) {
        UserEntity user = userRepository.findByUsername(username).orElseThrow(()-> new ResourceNotFoundException("User not found with username: " + username));

        HabitEntity newHabit = HabitEntity.builder()
                .title(habit.getTitle())
                .description(habit.getDescription())
                .startDate(new Date())
                .lastStreak(0)
                .user(user)
                .build();

        habitRepository.save(newHabit);

        return habitToDTO(newHabit);
    }

    @Override
    public HabitEntity deleteHabit(String id) {
        HabitEntity habit = habitRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Habit not found with id: " + id));
        habitRepository.delete(habit);
        return habit;
    }

    public HabitDTO habitToDTO(HabitEntity habit){
        HabitDTO habitDTO = new HabitDTO();
        habitDTO.setTitle(habit.getTitle());
        habitDTO.setDescription(habit.getDescription());
        habitDTO.setStartDate(habit.getStartDate());
        habitDTO.setEndDate(habit.getEndDate());
        habitDTO.setLastStreak(habit.getLastStreak());
        return habitDTO;
    }
}
