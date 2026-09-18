package com.mariaseverino.pinbook.dto;

import java.util.UUID;

public record AuthenticatedUser(
        UUID userId,
        String email,
        String role
) {}
