package com.mariaseverino.pinbook.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateLaneRequest(
    @NotBlank String name,
    @NotNull Integer capacity,
    @NotNull Float pricePerMinute
) {}
