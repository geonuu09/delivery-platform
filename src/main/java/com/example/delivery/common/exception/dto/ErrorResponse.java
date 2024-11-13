package com.example.delivery.common.exception.dto;

import com.example.delivery.common.exception.CustomException;
import com.example.delivery.common.exception.code.ErrorCode;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ErrorResponse {

    private final boolean success;
    private final String message;
    private final int status;
    private final String path;
    private final LocalDateTime timestamp;
    private final Object details;

    // CustomException을 처리하는 메서드
    public static ErrorResponse of(CustomException exception, String path) {
        return ErrorResponse.builder()
            .success(false)
            .message(exception.getMessage()) // CustomException 메시지 사용
            .status(exception.getErrorCode().getStatus().value())
            .path(path)
            .timestamp(LocalDateTime.now())
            .build();
    }

    // ErrorCode와 세부 정보를 처리하는 메서드
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

    // ErrorCode와 요청 URI만 처리하는 간단한 메서드
    public static ErrorResponse of(ErrorCode errorCode, String requestURI) {
        return ErrorResponse.builder()
            .success(false)
            .message(errorCode.getMessage())
            .status(errorCode.getStatus().value())
            .path(requestURI)
            .timestamp(LocalDateTime.now())
            .build();
    }
}