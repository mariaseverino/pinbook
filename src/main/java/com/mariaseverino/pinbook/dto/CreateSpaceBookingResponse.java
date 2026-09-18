package com.mariaseverino.pinbook.dto;

import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.util.UUID;

public record CreateSpaceBookingResponse(
    UUID id,
    Instant startTime,
    Integer durationMinutes,
    Float price,
    String clientName,
    String spaceName
) {

}
