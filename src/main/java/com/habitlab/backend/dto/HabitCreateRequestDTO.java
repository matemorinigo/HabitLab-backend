package com.habitlab.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class HabitCreateRequestDTO {
    private String title;
    private String description;
}
