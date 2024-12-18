package com.habitlab.backend.repository;


import com.habitlab.backend.persistance.entity.HabitEntity;
import com.habitlab.backend.persistance.entity.OccurrenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/*
* TODO add pagination
* */

@Repository
public interface OccurrenceRepository extends JpaRepository<OccurrenceEntity, UUID> {
    List<OccurrenceEntity> findAllByHabit(HabitEntity habit);
    List<OccurrenceEntity> findAllByHabitAndDateBetween(HabitEntity habit, Date startDate, Date endDate);
    Optional<OccurrenceEntity> findOccurrenceEntityByDate(Date date);
}
