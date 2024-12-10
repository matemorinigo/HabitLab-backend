package com.habitlab.backend.service;

import com.habitlab.backend.dto.HabitCreateRequestDTO;
import com.habitlab.backend.dto.HabitDTO;
import com.habitlab.backend.dto.PaginatedHabitsResponseDTO;
import com.habitlab.backend.exception.ResourceNotFoundException;

import com.habitlab.backend.persistance.entity.HabitEntity;
import com.habitlab.backend.persistance.entity.UserEntity;
import com.habitlab.backend.repository.HabitRepository;
import com.habitlab.backend.repository.UserRepository;
import com.habitlab.backend.util.ValidationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;



@Service
public class HabitService implements IHabitService {

    @Autowired
    private HabitRepository habitRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ValidationUtils validationUtils;

    @Override
    public PaginatedHabitsResponseDTO getHabits(String username, LocalDate startDate, int size) {

        Pageable pageable = PageRequest.of(0, size);
        List<HabitEntity> habits;

        UserEntity user = userRepository.findByUsername(username).orElseThrow();

        if(startDate == null){
            habits = habitRepository.findAllByUser(user.getId(), pageable);
        } else {
            Date date = Date.from(startDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

            habits = habitRepository.findByUserAndCursor(user.getId(), date, pageable);

        }


        LocalDate nextStartDate = habits.isEmpty() ? null : habits.getLast().getStartDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        return new PaginatedHabitsResponseDTO(habits
                .stream()
                .map(this::habitToDTO)
                .toList(), nextStartDate);
    }

    @Override
    public HabitDTO getHabit(String id, String username) {

        return habitToDTO(validationUtils.validateHabitOwner(id, username));
    }

    @Override
    public List<HabitDTO> getHabitsByTitle(String title, String username) {

        UserEntity user = userRepository.findByUsername(username).orElseThrow();

        return habitRepository.findByTitleContainingIgnoreCaseAndUser(title, user)
                .stream()
                .map(this::habitToDTO)
                .toList();
    }

    @Override
    public List<HabitDTO> getHabitsByStartDateBetween(LocalDate afterLocalDate, LocalDate beforeLocalDate, String username) {
        Date afterDate = Date.from(afterLocalDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date beforeDate = Date.from(beforeLocalDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

        UserEntity user = userRepository.findByUsername(username).orElseThrow();

        return habitRepository.findByStartDateBetweenAndUser(afterDate, beforeDate, user)
                .stream()
                .map(this::habitToDTO)
                .toList();
    }

    @Override
    public HabitDTO updateHabit(String id, HabitDTO updatedHabit, String username) {
        HabitEntity habit = validationUtils.validateHabitOwner(id, username);

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

        return habitToDTO(habitRepository.save(habit));
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
    public HabitDTO deleteHabit(String id, String username) {
        HabitEntity habit = validationUtils.validateHabitOwner(id, username);
        habitRepository.delete(habit);
        return habitToDTO(habit);
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void resetStreaks(){
        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1);
        List<HabitEntity> habitsToReset = habitRepository.findHabitsWithoutOccurrenceForDate(yesterday);

        for (HabitEntity habit : habitsToReset) {
            habit.setLastStreak(0);
        }

        habitRepository.saveAll(habitsToReset);
    }

    public HabitDTO habitToDTO(HabitEntity habit){
        HabitDTO habitDTO = new HabitDTO();
        habitDTO.setId(habit.getId());
        habitDTO.setTitle(habit.getTitle());
        habitDTO.setDescription(habit.getDescription());
        habitDTO.setStartDate(habit.getStartDate());
        habitDTO.setEndDate(habit.getEndDate());
        habitDTO.setLastStreak(habit.getLastStreak());
        return habitDTO;
    }


}
