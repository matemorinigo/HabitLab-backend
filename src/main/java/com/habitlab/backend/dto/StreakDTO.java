package com.habitlab.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class StreakDTO {
    private long streak;
    private String habitTitle;
}
