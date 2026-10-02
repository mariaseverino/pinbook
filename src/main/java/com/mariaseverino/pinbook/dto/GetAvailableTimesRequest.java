package com.mariaseverino.pinbook.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.UUID;

public record GetAvailableTimesRequest(
    @NotNull UUID spaceId,
    @NotNull int periodInMinutes
//    @NotNull LocalDate date
) {}
