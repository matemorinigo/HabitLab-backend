package com.habitlab.backend.repository;

import com.habitlab.backend.persistance.entity.HabitEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HabitRepository extends JpaRepository<HabitEntity, String> {
    Optional<HabitEntity> findByTitle(String title);
}
