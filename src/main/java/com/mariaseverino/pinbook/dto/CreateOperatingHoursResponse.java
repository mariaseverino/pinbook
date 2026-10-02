package com.mariaseverino.pinbook.dto;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

public record CreateOperatingHoursResponse(
        List<OperatingHourItem> hours
) {
    public record OperatingHourItem(
            UUID id,
            DayOfWeek weekDay,
            boolean active,
            LocalTime openingTime,
            LocalTime closingTime
    ) {}
}