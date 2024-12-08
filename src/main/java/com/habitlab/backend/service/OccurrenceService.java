package com.habitlab.backend.service;

import com.habitlab.backend.dto.OccurrenceViewDTO;

import com.habitlab.backend.persistance.entity.HabitEntity;
import com.habitlab.backend.persistance.entity.OccurrenceEntity;
import com.habitlab.backend.persistance.entity.UserEntity;

import com.habitlab.backend.repository.OccurrenceRepository;
import com.habitlab.backend.repository.UserRepository;
import com.habitlab.backend.util.ValidationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;


@Service
public class OccurrenceService implements IOcurrenceService {

    @Autowired
    private OccurrenceRepository occurrenceRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ValidationUtils validationUtils;

    public List<OccurrenceViewDTO> getOccurrencesByHabitId(String habitId, String username) {

        HabitEntity habit = validationUtils.validateHabitOwner(habitId, username);

        return occurrenceRepository.findAllByHabit(habit)
                .stream()
                .map(occurrence -> occurrenceToDTO(occurrence, habit, username))
                .toList();
    }

    public OccurrenceViewDTO createOccurrence(String habitId, String username) {
        HabitEntity habit = validationUtils.validateHabitOwner(habitId, username);
        UserEntity user = userRepository.findByUsername(username).orElseThrow();
        OccurrenceEntity occurrence = new OccurrenceEntity();
        occurrence.setHabit(habit);
        occurrence.setDate(new Date());
        occurrence.setUser(user);
        occurrenceRepository.save(occurrence);

        return occurrenceToDTO(occurrence, habit, username);
    }

    public List<OccurrenceViewDTO> getOccurrancesBetweenDates(String habitId, String username,LocalDate startDate, LocalDate endDate) {
        HabitEntity habit = validationUtils.validateHabitOwner(habitId, username);

        Date start = Date.from(startDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date end = Date.from(endDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

        return occurrenceRepository.findAllByHabitAndDateBetween(habit, start, end)
                .stream()
                .map(occurrence -> occurrenceToDTO(occurrence, habit, username))
                .toList();
    }

    private OccurrenceViewDTO occurrenceToDTO(OccurrenceEntity occurrence, HabitEntity habit, String username) {
        OccurrenceViewDTO dto = new OccurrenceViewDTO();
        dto.setId(occurrence.getId());
        dto.setOccurrenceDate(occurrence.getDate());
        dto.setHabitId(habit.getId());
        dto.setTitle(habit.getTitle());
        dto.setUserId(habit.getUser().getId());
        dto.setUsername(username);
        return dto;

    }
}
