package com.habitlab.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OccurrenceViewDTO {
    private UUID id;
    private UUID habitId;
    private String title;
    private UUID userId;
    private String username;
    private Date occurrenceDate;
}
