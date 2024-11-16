package com.example.delivery.common.exception.code;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    // Auth 관련 에러
    INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, "이메일 또는 비밀번호가 일치하지 않습니다."),
    UNAUTHORIZED_USER(HttpStatus.UNAUTHORIZED, "인증되지 않은 사용자입니다."),
    INVALID_PERMISSION(HttpStatus.FORBIDDEN, "권한이 없습니다."),

    // User 관련 에러
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),
    DUPLICATE_EMAIL(HttpStatus.CONFLICT, "이미 사용 중인 이메일입니다."),
    USER_ALREADY_DELETE(HttpStatus.BAD_REQUEST, "사용자가 이미 삭제되었습니다."),

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
    CART_MODIFICATION_NOT_ALLOWED(HttpStatus.BAD_REQUEST, "해당 장바구니는 수정할 수 없습니다."),
    CART_DELETION_NOT_ALLOWED(HttpStatus.BAD_REQUEST, "해당 장바구니는 삭제할 수 없습니다."),
    CANNOT_ADD_DIFFERENT_STORE_MENU(HttpStatus.BAD_REQUEST, "같은 가게 메뉴만 담으실 수 있습니다."),
    CANNOT_ADD_MENU_FROM_UNOWNED_STORE(HttpStatus.BAD_REQUEST, "자신 가게 메뉴만 담으실 수 있습니다."),
    NOT_FOUND_MENU_OPTION(HttpStatus.NOT_FOUND, "해당 옵션를 찾을 수 없습니다."),
    INVALID_MENU_COUNT(HttpStatus.BAD_REQUEST, "메뉴는 1개 이상부터 주문이 가능합니다."),

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

    // 리뷰 에러
    REVIEW_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 리뷰를 찾을 수 없습니다."),
    REVIEW_NOT_MATCH_BY_USER(HttpStatus.FORBIDDEN,"리뷰의 작성자와 요청자가 일치하지 않습니다."),
    REVIEW_DELETE_ALREADY(HttpStatus.BAD_REQUEST,"이 리뷰는 이미 삭제되었습니다."),
    REVIEW_NOT_MATCH_USER(HttpStatus.BAD_REQUEST,"회원이 쓴 리뷰가 아닙니다."),

    // 결제 에러
    PAYMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "결제를 찾을 수 없습니다."),

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