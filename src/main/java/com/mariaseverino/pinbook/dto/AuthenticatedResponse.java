package com.mariaseverino.pinbook.dto;

public record AuthenticatedResponse(
        String accessToken,
        String refreshToken,
        String tokenType
) {
    public AuthenticatedResponse(String accessToken, String refreshToken) {
        this(accessToken, refreshToken, "Bearer");
    }
}
