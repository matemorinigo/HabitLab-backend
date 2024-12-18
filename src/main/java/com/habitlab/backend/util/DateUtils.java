package com.habitlab.backend.util;

import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Component
public class DateUtils {
    public static int getMaxStreak(List<Date> occurrences) {

        List<LocalDate> dates = new ArrayList<>();
        for (Date date : occurrences) {
            dates.add(toLocalDate(date));
        }


        Collections.sort(dates);

        if(!occurrences.isEmpty()){
            int maxStreak = 1;
            int currentStreak = 1;


            for (int i = 1; i < dates.size(); i++) {
                if (dates.get(i).minusDays(1).equals(dates.get(i-1))) {
                    currentStreak++;
                } else {
                    currentStreak = 1;
                }
                maxStreak = Math.max(maxStreak, currentStreak);
            }

            return maxStreak;
        } else
            return 0;

    }

    public static LocalDate toLocalDate(Date date) {
        return date.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();
    }
}
