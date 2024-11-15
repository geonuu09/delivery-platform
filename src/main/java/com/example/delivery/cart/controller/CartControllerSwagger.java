package com.example.delivery.cart.controller;

import com.example.delivery.auth.security.UserDetailsImpl;
import com.example.delivery.cart.dto.request.CartCreateRequestDto;
import com.example.delivery.cart.dto.request.CartUpdateRequestDto;
import com.example.delivery.cart.dto.response.CartResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Cart API", description = "장바구니 관리 API")
public interface CartControllerSwagger {

    @Operation(
            summary = "장바구니 생성",
            description = "새로운 장바구니를 생성합니다."
    )
    @PostMapping
    ResponseEntity<CartResponseDto> createCart(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody CartCreateRequestDto requestDto
    );

    @Operation(
            summary = "장바구니 목록 조회",
            description = "장바구니 목록을 조회합니다."
    )
    @GetMapping
    ResponseEntity<List<CartResponseDto>> getCartList(
            @AuthenticationPrincipal UserDetailsImpl userDetails
    );

    @Operation(
            summary = "장바구니 수정",
            description = "대기 상태의 장바구니를 수정합니다."
    )
    @PutMapping
    ResponseEntity<CartResponseDto> updateCart(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody CartUpdateRequestDto requestDto
    );

    @Operation(
            summary = "장바구니 삭제",
            description = "장바구니를 삭제합니다."
    )
    @DeleteMapping("/{cartId}/delete")
    ResponseEntity<CartResponseDto> deleteCart(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable UUID cartId
    );

}
