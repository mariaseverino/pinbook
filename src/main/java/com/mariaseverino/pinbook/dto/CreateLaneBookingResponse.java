package com.mariaseverino.pinbook.dto;

import java.time.Instant;
import java.util.UUID;

public record CreateLaneBookingResponse(
    UUID id,
    Instant startTime,
    Integer durationMinutes,
    Float price,
    String clientName,
    String spaceName,
    String laneName
) {}
