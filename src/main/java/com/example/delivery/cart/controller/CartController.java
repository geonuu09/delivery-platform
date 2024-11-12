package com.example.delivery.cart.controller;

import com.example.delivery.auth.security.UserDetailsImpl;
import com.example.delivery.cart.dto.request.CartCreateRequestDto;
import com.example.delivery.cart.dto.response.CartResponseDto;
import com.example.delivery.cart.service.CartService;
import com.example.delivery.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/carts")
public class CartController {

    private final CartService cartService;

    // 장바구니 생성
    @PostMapping
    public ResponseEntity<CartResponseDto> createCart(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody CartCreateRequestDto requestDto
    ){
        User user = userDetails.getUser();
        CartResponseDto responseDto = cartService.createCart(user, requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    // 장바구니 조회
    // 장바구니 수정
    // 장바구니 삭제
}
