package com.example.delivery.exception.dto;

import com.example.delivery.exception.code.ErrorCode;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ErrorResponse {
    private final boolean success;
    private final String message;
    private final int status;
    private final String path;
    private final LocalDateTime timestamp;
    private final Object details;

    public static ErrorResponse of(ErrorCode errorCode, String path) {
        return ErrorResponse.builder()
            .success(false)
            .message(errorCode.getMessage())
            .status(errorCode.getStatus().value())
            .path(path)
            .timestamp(LocalDateTime.now())
            .build();
    }

    public static ErrorResponse of(ErrorCode errorCode, String path, Object details) {
        return ErrorResponse.builder()
            .success(false)
            .message(errorCode.getMessage())
            .status(errorCode.getStatus().value())
            .path(path)
            .timestamp(LocalDateTime.now())
            .details(details)
            .build();
    }
}