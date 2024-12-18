package com.habitlab.backend.repository;

import com.habitlab.backend.persistance.entity.HabitEntity;
import com.habitlab.backend.persistance.entity.NoteEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface NoteRepository extends JpaRepository<NoteEntity, UUID> {
    List<NoteEntity> findAllByHabit(HabitEntity habit);
}
