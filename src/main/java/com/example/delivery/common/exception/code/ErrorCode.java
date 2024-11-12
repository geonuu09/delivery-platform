package com.example.delivery.common.exception.code;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    // Auth 관련 에러
    INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, "이메일 또는 비밀번호가 일치하지 않습니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "인증되지 않은 요청입니다."),
    UNAUTHORIZED_USER(HttpStatus.UNAUTHORIZED, "인증되지 않은 사용자입니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다."),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "토큰이 만료되었습니다."),
    INVALID_PERMISSION(HttpStatus.FORBIDDEN, "권한이 없습니다."),

    // User 관련 에러
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),
    DUPLICATE_EMAIL(HttpStatus.CONFLICT, "이미 사용 중인 이메일입니다."),
    INVALID_ROLE(HttpStatus.BAD_REQUEST, "유효하지 않은 사용자 권한입니다."),

    // Order 관련 에러
    NOT_FOUND_STORE(HttpStatus.NOT_FOUND, "해당 가게를 찾을 수 없습니다."),
    NOT_FOUND_ORDER(HttpStatus.NOT_FOUND, "해당 주문을 찾을 수 없습니다."),
    ORDER_PERMISSION_DENIED(HttpStatus.FORBIDDEN, "해당 주문에 대한 접근 권한이 없습니다."),
    ORDER_CANCELLATION_TIMEOUT(HttpStatus.BAD_REQUEST, "주문은 5분 이내에만 취소 가능합니다."),

    // Cart 관련 에러
    NOT_FOUND_MENU(HttpStatus.NOT_FOUND, "해당 메뉴를 찾을 수 없습니다."),
    NOT_FOUND_CART(HttpStatus.NOT_FOUND, "해당 장바구니를 찾을 수 없습니다."),
    UNAVAILABLE_MENU(HttpStatus.BAD_REQUEST, "해당 메뉴는 주문하실 수 없습니다."),
    CART_PERMISSION_DENIED(HttpStatus.FORBIDDEN, "해당 장바구니에 대한 접근 권한이 없습니다."),

    // 공통 에러
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "유효하지 않은 입력값입니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류가 발생했습니다.");

    private final HttpStatus status;
    private final String message;

    ErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}