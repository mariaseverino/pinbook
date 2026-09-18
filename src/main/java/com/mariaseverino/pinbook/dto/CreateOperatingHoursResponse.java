package com.mariaseverino.pinbook.dto;

import com.mariaseverino.pinbook.entity.OperatingHour;

import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

public record CreateOperatingHoursResponse(
        List<OperatingHourItem> hours
) {
    public record OperatingHourItem(
            UUID id,
            OperatingHour.WeekDay weekDay,
            boolean active,
            LocalTime openingTime,
            LocalTime closingTime
    ) {}
}