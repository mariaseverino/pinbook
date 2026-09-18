package com.mariaseverino.pinbook.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.util.UUID;

public record CreateSpaceBookingRequest(
    @NotNull Instant startTime,
    @NotNull @Min(30) @Max(240) Integer durationMinutes,
    @NotNull UUID clientId,
    @NotNull UUID spaceId
) {
}
