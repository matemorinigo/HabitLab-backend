package com.habitlab.backend.dto;

import lombok.Data;

import java.util.Date;

@Data
public class HabitDTO {
    private String title;
    private String description;
    private Date startDate;
    private Date endDate;
    private int lastStreak;
}
