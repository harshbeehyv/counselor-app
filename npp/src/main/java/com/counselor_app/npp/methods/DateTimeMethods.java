package com.counselor_app.npp.methods;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DateTimeMethods {
    public static String convertToYYYYMMDD(String dateString) {
        // Define the formatter for the input date format
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("d MMMM yyyy", Locale.ENGLISH);

        // Parse the input date string into a LocalDate object
        LocalDate date = LocalDate.parse(dateString, inputFormatter);

        // Define the formatter for the output format (yyyy-MM-dd)
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // Format the LocalDate object into the desired output format
        String formattedDate = date.format(outputFormatter);

        return formattedDate;
    }
}
