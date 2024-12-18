package com.habitlab.backend.service;

import com.habitlab.backend.dto.*;
import com.habitlab.backend.persistance.entity.HabitEntity;

import java.time.LocalDate;
import java.util.List;

public interface IHabitService {

     PaginatedHabitsResponseDTO getHabits(String username , LocalDate startDate, int size);
     HabitDTO getHabit(String id, String username);
     List<HabitDTO> getHabitsByTitle(String title, String username);
     List<HabitDTO> getHabitsByStartDateBetween(LocalDate afterLocalDate, LocalDate beforeLocalDate, String username);
     List<HabitDTO> getAllHabits(String username);

     HabitDTO updateHabit(String id, HabitDTO updatedHabit, String username);

     HabitDTO saveHabit(HabitCreateRequestDTO habit, String username);

     HabitDTO deleteHabit(String id, String username);

     void resetStreaks();

     List<NoteDTO> getHabitNotes(String habitId, String username);
     NoteDTO createNote(String id, String username, NoteBodyDTO note, String occurranceId);

     HabitDTO habitToDTO(HabitEntity habitEntity);
}
