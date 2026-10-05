package com.mariaseverino.pinbook.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


public record UpdateSpaceRequest(
    @NotBlank String name,
    @NotBlank String description,
    @NotNull @Min(1) Integer capacity,
    @NotNull Integer batchMaintenanceTime,
    @NotNull Float pricePerMinute
) {}
