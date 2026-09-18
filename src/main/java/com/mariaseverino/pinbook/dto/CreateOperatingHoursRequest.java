package com.mariaseverino.pinbook.dto;

import com.mariaseverino.pinbook.entity.OperatingHour;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

public record CreateOperatingHoursRequest(
        @NotEmpty @Valid List<OperatingHourItem> hours
) {
    public record OperatingHourItem(
            @NotNull OperatingHour.WeekDay weekDay,
            @NotNull boolean active,
            @NotNull LocalTime openingTime,
            @NotNull LocalTime closingTime
    ) {}
}
