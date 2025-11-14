package com.example.cervejaria_api.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateUtils {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static LocalDate parse(String date) {
        return LocalDate.parse(date, formatter);
    }

    public static String format(LocalDate date) {
        return date.format(formatter);
    }
}
