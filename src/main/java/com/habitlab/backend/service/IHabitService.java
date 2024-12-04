package com.habitlab.backend.service;

import com.habitlab.backend.dto.HabitDTO;
import com.habitlab.backend.persistance.entity.HabitEntity;
import jakarta.websocket.MessageHandler;

import java.util.List;

public interface IHabitService {

     List<HabitEntity> getHabits();
     HabitEntity getHabit(String id);
     HabitEntity getHabitByTitle(String title);

     HabitEntity updateHabit(String id, HabitDTO updatedHabit);

     HabitEntity saveHabit(HabitDTO habit);

     HabitEntity deleteHabit(String id);

}
