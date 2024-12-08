package com.habitlab.backend.repository;

import com.habitlab.backend.persistance.entity.HabitEntity;
import com.habitlab.backend.persistance.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface HabitRepository extends JpaRepository<HabitEntity, String> {
    Optional<HabitEntity> findById(UUID id);

    List<HabitEntity> findAllByUser(UserEntity user);

    Optional<HabitEntity> findTopByUserOrderByLastStreakDesc(UserEntity user);

    @Query("SELECT h FROM HabitEntity h WHERE NOT EXISTS (" +
            "SELECT o FROM OccurrenceEntity o WHERE o.habit = h AND o.date = :date)")
    List<HabitEntity> findHabitsWithoutOccurrenceForDate(@Param("date") LocalDate date);

    List<HabitEntity> findByStartDateBetween(Date startDateAfter, Date startDateBefore);

    List<HabitEntity>  findByTitleContainingIgnoreCase(String title);
}
