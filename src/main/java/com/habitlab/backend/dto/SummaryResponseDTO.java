package com.habitlab.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class SummaryResponseDTO {
    private double accomplishmentPercentage;
    private int totalHabits;
    private List<HabitDTO> newHabits;
    private List<StreakDTO> streaks;
}
