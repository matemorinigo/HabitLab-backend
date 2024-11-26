package com.habitlab.backend.service;

import com.habitlab.backend.dto.HabitDTO;
import com.habitlab.backend.exception.ResourceNotFoundException;
import com.habitlab.backend.persistance.entity.HabitEntity;
import com.habitlab.backend.repository.HabitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class HabitService implements IHabitService {

    @Autowired
    private HabitRepository habitRepository;

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
    public HabitEntity saveHabit(HabitDTO habit) {
        HabitEntity newHabit = HabitEntity.builder()
                .title(habit.getTitle())
                .description(habit.getDescription())
                .startDate(habit.getStartDate())
                .endDate(habit.getEndDate())
                .build();
        return habitRepository.save(newHabit);
    }

    @Override
    public HabitEntity deleteHabit(String id) {
        HabitEntity habit = habitRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Habit not found with id: " + id));
        habitRepository.delete(habit);
        return habit;
    }
}
