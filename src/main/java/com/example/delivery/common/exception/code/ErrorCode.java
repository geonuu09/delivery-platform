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

    // 공통 에러
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "유효하지 않은 입력값입니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류가 발생했습니다."),

    // Store 관련 에러
    STORE_NOT_FOUND(HttpStatus.NOT_FOUND, "가게를 찾을 수 없습니다."),
    STORE_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 등록된 가게입니다."),
    STORE_UPDATE_FAILED(HttpStatus.BAD_REQUEST, "가게 정보를 업데이트할 수 없습니다."),
    STORE_DELETE_FAILED(HttpStatus.BAD_REQUEST, "가게 삭제에 실패했습니다."),
    INVALID_STORE_STATUS(HttpStatus.BAD_REQUEST, "유효하지 않은 가게 상태입니다."),

    // Category 관련 에러
    CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "카테고리를 찾을 수 없습니다."),
    CATEGORY_UPDATE_FAILED(HttpStatus.BAD_REQUEST, "가게 정보를 업데이트할 수 없습니다."),
    CATEGORY_DELETE_FAILED(HttpStatus.BAD_REQUEST, "카테고리 삭제에 실패했습니다."),


    // Menu 관련 에러
    MENU_NOT_FOUND(HttpStatus.NOT_FOUND, "메뉴를 찾을 수 없습니다."),
    MENU_UPDATE_FAILED(HttpStatus.BAD_REQUEST, "메뉴를 업데이트할 수 없습니다."),
    MENU_DELETE_FAILED(HttpStatus.BAD_REQUEST, "메뉴삭제에 실패했습니다."),

    // Menu Option 관련 에러
    MENU_OPTION_NOT_FOUND(HttpStatus.NOT_FOUND, "메뉴 옵션을 찾을 수 없습니다."),
    MENU_OPTION_UPDATE_FAILED(HttpStatus.BAD_REQUEST, "메뉴 옵션을 업데이트할 수 없습니다."),
    MENU_OPTION_DELETE_FAILED(HttpStatus.BAD_REQUEST, "메뉴 옵션 삭제에 실패했습니다.");

    private final HttpStatus status;
    private final String message;

    ErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}