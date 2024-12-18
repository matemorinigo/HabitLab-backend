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
@Table(name = "notes")
public class NoteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    UserEntity user;

    @ManyToOne
    @JoinColumn(name = "habit_id", nullable = false)
    HabitEntity habit;

    @ManyToOne
    @JoinColumn(name = "occurrence_id")
    OccurrenceEntity occurrence;

    @Column(name = "createdAt", nullable = false)
    Date createdAt;

    @Column(name = "deletedAt")
    Date deletedAt;

    @Column(name = "content", nullable = false, length = 500)
    String content;

}
