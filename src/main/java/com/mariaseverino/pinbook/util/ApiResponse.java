package com.mariaseverino.pinbook.util;

import com.mariaseverino.pinbook.dto.SuccessResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ApiResponse {
    private ApiResponse() {}

    public static <T> ResponseEntity<SuccessResponse<T>> created(T data, String message) {
        return ResponseEntity.status(HttpStatus.CREATED).body(SuccessResponse.success(data, message));
    }

    public static <T> ResponseEntity<SuccessResponse<T>> ok(T data, String message) {
        return ResponseEntity.ok(SuccessResponse.success(data, message));
    }
}
