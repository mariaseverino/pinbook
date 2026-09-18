package com.mariaseverino.pinbook.dto;

import java.util.UUID;

public record CreateLaneResponse(
        UUID id,
        String name,
        Integer capacity,
        UUID spaceId
) {}
