package com.habitlab.backend.persistance.entity;

import jakarta.persistence.*;
import lombok.*;

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

    /*
    * TODO This could be redundant
    * */

    @ManyToOne
    @JoinColumn(name = "habit_id")
    private HabitEntity habit;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Column(name = "date", nullable = false)
    private Date date;

}
