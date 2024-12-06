package com.habitlab.backend.persistance.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

@Setter
@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "occurrences",
        uniqueConstraints = @UniqueConstraint(columnNames = {"habit_id", "user_id", "date"}))
public class OccurrenceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @MapsId
    @JoinColumn(name = "habit_id")
    private HabitEntity habit;

    @ManyToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Column(name = "date", nullable = false)
    private LocalDate date;

}
