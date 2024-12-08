package com.habitlab.backend.service;

import com.habitlab.backend.dto.OccurrenceViewDTO;

import java.time.LocalDate;
import java.util.List;

public interface IOcurrenceService {
    List<OccurrenceViewDTO> getOccurrencesByHabitId(String habitId, String username);

    OccurrenceViewDTO createOccurrence(String habitId, String username);

    List<OccurrenceViewDTO> getOccurrancesBetweenDates(String habitId, String username, LocalDate startDate, LocalDate endDate);
}
