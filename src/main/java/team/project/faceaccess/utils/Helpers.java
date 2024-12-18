package team.project.faceaccess.utils;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Helpers {

    public static String getDateTimeatStartOfDay() {
        LocalDate today = LocalDate.now();

        // Set the time to midnight (00:00:00)
        LocalDateTime midnight = today.atStartOfDay();

        // Define the desired format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.DATETIME_PATTERN);

        // Format the date and time
        return midnight.format(formatter);
    }

    public static String getDateTimeAtStartOfWeek() {
        LocalDate today = LocalDate.now();

        // Find the Monday of this week
        LocalDate monday = today.with(DayOfWeek.MONDAY);

        // Set the time to 00:00:00
        LocalDateTime mondayMidnight = monday.atStartOfDay();

        // Define the desired format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.DATETIME_PATTERN);

        // Format the date and time
        return mondayMidnight.format(formatter);
    }

    public static String getDateTimeatStartOfMonth() {
        LocalDate today = LocalDate.now();

        // Get the first day of the current month
        LocalDate firstDayOfMonth = today.withDayOfMonth(1);

        // Set the time to 00:00:00
        LocalDateTime startOfMonth = firstDayOfMonth.atStartOfDay();

        // Define the desired format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.DATETIME_PATTERN);

        // Format the date and time
        return startOfMonth.format(formatter);
    }

    public static Map<String, String> getLastDaysNames() {
        Locale enLocal = new Locale("en", "EN");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE",enLocal);
        Map<String, String> days = new HashMap<>();
        LocalDate today = LocalDate.now();
        days.put("today", "today");
        days.put("today1", "yesterday");
        days.put("today2", today.minusDays(2).format(formatter));
        days.put("today3", today.minusDays(3).format(formatter));
        days.put("today4", today.minusDays(4).format(formatter));
        days.put("today5", today.minusDays(5).format(formatter));
        days.put("today6", today.minusDays(6).format(formatter));
        return days;
    }
}
