package com.habitlab.backend.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class UserProfileDTO {
    /*
    * TODO 1: Add profile picture with AWS S3
    * */
    private String username;
    private List<HabitDTO> habits;
    private int largestStreak;
    private int totalHabits;
}
