package com.mariaseverino.pinbook.util;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneId;

public class DateTimeUtils {
    private static final ZoneId APP_ZONE = ZoneId.of("America/Sao_Paulo");

    private DateTimeUtils() {
    }

    public static LocalTime toLocalTime(Instant instant) {
        return instant.atZone(APP_ZONE).toLocalTime();
    }

    public static java.time.DayOfWeek toDayOfWeek(Instant instant) {
        return instant.atZone(APP_ZONE).getDayOfWeek();
    }
}
