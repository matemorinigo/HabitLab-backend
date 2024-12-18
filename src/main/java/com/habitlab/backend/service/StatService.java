package com.habitlab.backend.service;

import com.habitlab.backend.dto.HabitDTO;
import com.habitlab.backend.dto.OccurrenceViewDTO;
import com.habitlab.backend.dto.StreakDTO;
import com.habitlab.backend.dto.SummaryResponseDTO;
import com.habitlab.backend.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class StatService {
    @Autowired
    private HabitService habitService;

    @Autowired
    private OccurrenceService ocurrenceService;

    @Autowired
    private OccurrenceService occurrenceService;


    public SummaryResponseDTO getSummary(String username, LocalDate dateAfter, LocalDate dateBefore) {
        if(dateAfter == null) {
            dateAfter = LocalDate.now().minusDays(30);
        }

        if(dateBefore == null) {
            dateBefore = LocalDate.now().plusDays(1);
        }

        List<HabitDTO> newHabits = habitService.getHabitsByStartDateBetween(dateAfter, dateBefore, username);
        List<HabitDTO> allHabits = habitService.getAllHabits(username);

        long dias = ChronoUnit.DAYS.between(dateAfter, dateBefore);

        List<StreakDTO> streaks = new ArrayList<>();
        List<Double> occurrencesPercentagePerHabit = new ArrayList<>();


        for(HabitDTO habit : allHabits) {
            StreakDTO streak = new StreakDTO();
            List<OccurrenceViewDTO> occurrences = occurrenceService.getOccurrancesBetweenDates(habit.getId().toString(), username, dateAfter, dateBefore);
            streak.setStreak(getLongestStreak(occurrences));
            streak.setHabitTitle(habit.getTitle());
            streaks.add(streak);
            occurrencesPercentagePerHabit.add((double) occurrences.size()/dias);
        }

        SummaryResponseDTO summary = new SummaryResponseDTO();

        summary.setNewHabits(newHabits);
        summary.setAccomplishmentPercentage((double) occurrencesPercentagePerHabit.stream().mapToDouble(Double::doubleValue).sum()/allHabits.size());
        summary.setTotalHabits(allHabits.size());
        summary.setStreaks(streaks);


        return summary;
    }

    private long getLongestStreak(List<OccurrenceViewDTO> occurrences) {
        List<Date> dates = new ArrayList<>();

        for(OccurrenceViewDTO occurence: occurrences){
            dates.add(occurence.getOccurrenceDate());
        }

        return DateUtils.getMaxStreak(dates);
    }
}
