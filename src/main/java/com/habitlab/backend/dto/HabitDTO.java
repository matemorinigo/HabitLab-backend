package com.habitlab.backend.dto;

import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
public class HabitDTO {
    private UUID id;
    private String title;
    private String description;
    private Date startDate;
    private Date endDate;
    private int lastStreak;
}
