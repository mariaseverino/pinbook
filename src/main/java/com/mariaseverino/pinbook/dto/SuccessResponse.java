package com.mariaseverino.pinbook.dto;

public record SuccessResponse<T>(
        boolean success,
        String message,
        T data,
        Object meta
) {
    public static <T> SuccessResponse<T> success(T data, String message) {
        return new SuccessResponse<>(true, message, data, null);
    }
}
