package com.kme.maileverday.utility;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class UtilityFunctions {

    public static LocalDateTime exchangeToLocalDateTime(String lastMailTime) {
        LocalDateTime dateTime;
        if (lastMailTime != null) {
            dateTime = LocalDateTime.parse(lastMailTime, DateTimeFormatter.RFC_1123_DATE_TIME);
        }
        else
            dateTime = null;
        return dateTime;
    }
}
