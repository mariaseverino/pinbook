package com.mariaseverino.pinbook.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreateLaneResponse(
        UUID id,
        String name,
        Integer capacity,
        Float pricePerMinute,
        UUID spaceId
) {}
