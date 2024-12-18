package com.habitlab.backend.service;

import com.habitlab.backend.dto.*;
import com.habitlab.backend.exception.ResourceNotFoundException;
import com.habitlab.backend.persistance.entity.HabitEntity;
import com.habitlab.backend.persistance.entity.NoteEntity;
import com.habitlab.backend.persistance.entity.OccurrenceEntity;
import com.habitlab.backend.persistance.entity.UserEntity;
import com.habitlab.backend.repository.HabitRepository;
import com.habitlab.backend.repository.NoteRepository;
import com.habitlab.backend.repository.OccurrenceRepository;
import com.habitlab.backend.repository.UserRepository;
import com.habitlab.backend.util.ValidationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class HabitService implements IHabitService {

    @Autowired
    private HabitRepository habitRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private OccurrenceRepository occurrenceRepository;

    @Autowired
    private ValidationUtils validationUtils;

    @Override
    public PaginatedHabitsResponseDTO getHabits(String username, LocalDate startDate, int size) {

        Pageable pageable = PageRequest.of(0, size);
        List<HabitEntity> habits;

        UserEntity user = userRepository.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if(startDate == null){
            habits = habitRepository.findAllByUser(user.getId(), pageable);
        } else {
            Date date = Date.from(startDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
            habits = habitRepository.findByUserAndCursor(user.getId(), date, pageable);
        }


        LocalDate nextStartDate = habits.isEmpty() ? null : habits.getLast().getStartDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        return new PaginatedHabitsResponseDTO(habits
                .parallelStream()
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
                .parallelStream()
                .map(this::habitToDTO)
                .toList();
    }

    @Override
    public List<HabitDTO> getHabitsByStartDateBetween(LocalDate afterLocalDate, LocalDate beforeLocalDate, String username) {
        Date afterDate = Date.from(afterLocalDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date beforeDate = Date.from(beforeLocalDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

        UserEntity user = userRepository.findByUsername(username).orElseThrow();

        return habitRepository.findByStartDateBetweenAndUser(afterDate, beforeDate, user)
                .parallelStream()
                .map(this::habitToDTO)
                .toList();
    }

    public List<HabitDTO> getAllHabits(String username){
        UserEntity user = userRepository.findByUsername(username).orElseThrow();
        return habitRepository.findAllByUser(user)
                .parallelStream()
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

    @Override
    public List<NoteDTO> getHabitNotes(String habitId, String username){
        HabitEntity habit = validationUtils.validateHabitOwner(habitId, username);
        return noteRepository.findAllByHabit(habit)
                .parallelStream()
                .map((this::noteToDTO))
                .toList();
    }

    @Override
    public NoteDTO createNote(String habitId, String username, NoteBodyDTO noteBody, String occurranceId){
        HabitEntity habit = validationUtils.validateHabitOwner(habitId, username);
        OccurrenceEntity occurrence = null;
        if(occurranceId != null)
            occurrence = occurrenceRepository.findById(UUID.fromString(occurranceId)).orElseThrow(() -> new ResourceNotFoundException("Occurrence not found with id: " + occurranceId));

        UserEntity user = userRepository.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));

        NoteEntity note = NoteEntity.builder()
                        .habit(habit)
                        .user(user)
                        .occurrence(occurrence)
                        .createdAt(new Date())
                        .content(noteBody.getContent())
                        .build();

        noteRepository.save(note);

        return noteToDTO(note);
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

    private NoteDTO noteToDTO(NoteEntity note){

        NoteDTO noteDTO = new NoteDTO();
        noteDTO.setId(note.getId());
        noteDTO.setHabitTitle(note.getHabit().getTitle());
        noteDTO.setUsername(note.getUser().getUsername());
        noteDTO.setOccurrenceId(note.getOccurrence() != null ? note.getOccurrence().getDate() : null);
        noteDTO.setContent(note.getContent());
        noteDTO.setCreatedAt(note.getCreatedAt());
        return noteDTO;
    }
}
