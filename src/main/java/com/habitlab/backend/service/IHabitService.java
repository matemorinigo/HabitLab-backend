package com.habitlab.backend.service;

import com.habitlab.backend.dto.HabitCreateRequestDTO;
import com.habitlab.backend.dto.HabitDTO;
import com.habitlab.backend.persistance.entity.HabitEntity;

import java.time.LocalDate;
import java.util.List;

public interface IHabitService {

     List<HabitDTO> getHabits(String username);
     HabitDTO getHabit(String id, String username);
     List<HabitDTO> getHabitsByTitle(String title, String username);
     List<HabitDTO> getHabitsByStartDateBetween(LocalDate afterLocalDate, LocalDate beforeLocalDate, String username);

     HabitDTO updateHabit(String id, HabitDTO updatedHabit, String username);

     HabitDTO saveHabit(HabitCreateRequestDTO habit, String username);

     HabitDTO deleteHabit(String id, String username);

     void resetStreaks();

     HabitDTO habitToDTO(HabitEntity habitEntity);
}
