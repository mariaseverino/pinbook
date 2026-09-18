package com.mariaseverino.pinbook.dto;

import java.util.UUID;

public record CreateSpaceResponse(
        UUID id,
        String name,
        String description,
        String cep,
        Integer capacity,
        Integer batchMaintenanceTime,
        Float pricePerMinute
) {}
