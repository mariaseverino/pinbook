package com.mariaseverino.pinbook.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreateLaneRequest(
        @NotBlank String name,
        @NotNull Integer capacity,
        @NotNull Float pricePerMinute,
        @NotNull UUID spaceId
) {}
