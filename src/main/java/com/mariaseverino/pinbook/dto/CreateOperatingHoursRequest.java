package com.mariaseverino.pinbook.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

public record CreateOperatingHoursRequest(
        @NotEmpty @Valid List<OperatingHourItem> hours
) {
    public record OperatingHourItem(
            @NotNull DayOfWeek weekDay,
            @NotNull boolean active,
            @NotNull LocalTime openingTime,
            @NotNull LocalTime closingTime
    ) {}
}
