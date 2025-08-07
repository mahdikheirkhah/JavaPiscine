import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class FormatDate {

    // 1. Format: Le 22 août de l'an 2021 à 13h25m et 46s
    public static String formatToFullText(LocalDateTime dateTime) {
        if(dateTime == null) return null;
        String day = String.valueOf(dateTime.getDayOfMonth());
        String month = dateTime.getMonth().getDisplayName(java.time.format.TextStyle.SHORT, Locale.FRENCH);
        int year = dateTime.getYear();
        int hour = dateTime.getHour();
        int minute = dateTime.getMinute();
        int second = dateTime.getSecond();

        return String.format("Le %s %s de l'an %d à %dh%dm et %ds", day, month, year, hour, minute, second);
    }

    // 2. Format: febbraio 13 22
    public static String formatSimple(LocalDate date) {
        if(date == null) return null;
        String month = date.getMonth().getDisplayName(java.time.format.TextStyle.FULL, Locale.ITALIAN);
        int day = date.getDayOfMonth();
        int year = date.getYear() % 100; // get last 2 digits

        return String.format("%s %d %02d", month, day, year);
    }

    // 3. Format: 16:18:56.008495847
    public static String formatIso(LocalTime time) {
        if(time == null) return null;
        return time.toString(); // Default ISO format
    }
}