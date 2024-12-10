package com.habitlab.backend.repository;

import com.habitlab.backend.persistance.entity.HabitEntity;
import com.habitlab.backend.persistance.entity.UserEntity;
import org.springframework.data.domain.Pageable;
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

    @Query("""
        SELECT h 
        FROM HabitEntity h 
        WHERE h.user.id = :userId
        ORDER BY h.startDate DESC
    """)
    List<HabitEntity> findAllByUser(@Param("userId") UUID userId, Pageable pageable);
    List<HabitEntity> findAllByUser(UserEntity user, Pageable pageable);

    Optional<HabitEntity> findTopByUserOrderByLastStreakDesc(UserEntity user);

    @Query("SELECT h FROM HabitEntity h WHERE NOT EXISTS (" +
            "SELECT o FROM OccurrenceEntity o WHERE o.habit = h AND o.date = :date)")
    List<HabitEntity> findHabitsWithoutOccurrenceForDate(@Param("date") LocalDate date);

    List<HabitEntity> findByStartDateBetweenAndUser(Date startDateAfter, Date startDateBefore, UserEntity user);

    List<HabitEntity>  findByTitleContainingIgnoreCaseAndUser(String title, UserEntity user);

    @Query("""
        SELECT h FROM HabitEntity h WHERE h.user.id = :userId
        AND h.startDate < :startDate
        ORDER BY h.startDate DESC
    """)
    List<HabitEntity> findByUserAndCursor(
            @Param("userId") UUID userId,
            @Param("startDate") Date startDate,
            Pageable pageable
    );

}
