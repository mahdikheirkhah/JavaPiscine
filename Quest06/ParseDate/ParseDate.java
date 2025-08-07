import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class ParseDate {

    public static LocalDateTime parseIsoFormat(String stringDate) {
        if(stringDate == null) return null;
        return LocalDateTime.parse(stringDate);
    }

    public static LocalDate parseFullTextFormat(String stringDate) {
        if(stringDate == null) return null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE d MMMM yyyy", Locale.FRENCH);
        return LocalDate.parse(stringDate, formatter);
    }

    public static LocalTime parseTimeFormat(String stringDate) {
        if(stringDate == null) return null;
        // Remove French words and get numbers
        String cleaned = stringDate
            .replace("heures du soir", "") // PM
            .replace("heures du matin", "") // AM
            .replace("minutes", "")
            .replace("et", "")
            .replace("secondes", "")
            .replace(",", "")
            .trim();

        String[] parts = cleaned.split("\\s+");

        int hour = Integer.parseInt(parts[0]);
        int minute = Integer.parseInt(parts[1]);
        int second = Integer.parseInt(parts[2]);

        // If it's "soir", we assume PM, add 12 hours unless it's 12 already
        if (stringDate.contains("soir") && hour < 12) {
            hour += 12;
        }

        return LocalTime.of(hour, minute, second);
    }

}