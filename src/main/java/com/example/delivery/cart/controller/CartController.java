package com.example.delivery.cart.controller;

import com.example.delivery.auth.security.UserDetailsImpl;
import com.example.delivery.cart.dto.request.CartCreateRequestDto;
import com.example.delivery.cart.dto.response.CartResponseDto;
import com.example.delivery.cart.service.CartService;
import com.example.delivery.user.entity.User;
import com.example.delivery.user.entity.UserRoleEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/carts")
public class CartController {

    private final CartService cartService;

    // 장바구니 생성
    @PostMapping
    @PreAuthorize("hasAnyRole('CUSTOMER','OWNER', 'MANAGER', 'MASTER')")
    public ResponseEntity<CartResponseDto> createCart(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody CartCreateRequestDto requestDto
    ){
        User user = userDetails.getUser();
        CartResponseDto responseDto = cartService.createCart(user, requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    // 장바구니 조회
    @GetMapping
    @PreAuthorize("hasAnyRole('CUSTOMER','OWNER', 'MANAGER', 'MASTER')")
    public ResponseEntity<List<CartResponseDto>> getCartList(
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ){
        Long userId = userDetails.getUser().getUserId();
        UserRoleEnum userRole = userDetails.getUser().getRole();

        if (userRole == UserRoleEnum.MANAGER || userRole == UserRoleEnum.MASTER) {
            List<CartResponseDto> orderList = cartService.getAllCartList();
            return ResponseEntity.ok(orderList);
        }

        List<CartResponseDto> orderList = cartService.getCartList(userId);
        return ResponseEntity.ok(orderList);
    }

    // 장바구니 수정
    // 장바구니 삭제
}
