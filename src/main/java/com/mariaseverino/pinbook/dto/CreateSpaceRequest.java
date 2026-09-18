package com.mariaseverino.pinbook.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateSpaceRequest(
        @NotBlank String name,
        @NotBlank String description,
        @NotBlank String cep,
        @NotNull @Min(1) Integer capacity,
        @NotNull Integer batchMaintenanceTime,
        @NotNull Float pricePerMinute
) {}
