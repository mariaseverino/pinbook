package com.mariaseverino.pinbook.dto;

import java.util.UUID;

public record UpdateLaneResponse(
    UUID id,
    String name,
    Integer capacity,
    Float pricePerMinute
) {}
