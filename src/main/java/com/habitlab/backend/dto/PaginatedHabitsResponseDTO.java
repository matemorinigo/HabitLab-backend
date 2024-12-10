package com.habitlab.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@Data
public class PaginatedHabitsResponseDTO {
    List<HabitDTO> habits;
    LocalDate nextStartDate;
}
