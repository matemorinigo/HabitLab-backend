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
@Table(name = "habits")
public class HabitEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "startDate", nullable = false)
    private Date startDate;
    @Column(name = "endDate")
    private Date endDate;

    @Column(name = "lastStreak")
    private int lastStreak;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    public HabitEntity() {
        lastStreak = 0;
    }

}
