package com.example.delivery.cart.controller;

import com.example.delivery.auth.security.UserDetailsImpl;
import com.example.delivery.cart.dto.request.CartCreateRequestDto;
import com.example.delivery.cart.dto.request.CartUpdateRequestDto;
import com.example.delivery.cart.dto.response.CartResponseDto;
import com.example.delivery.cart.service.CartCreateService;
import com.example.delivery.cart.service.CartDeleteService;
import com.example.delivery.cart.service.CartGetListService;
import com.example.delivery.cart.service.CartUpdateService;
import com.example.delivery.user.entity.UserRoleEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/carts")
public class CartController {

    private final CartCreateService cartCreateService;
    private final CartGetListService cartGetListService;
    private final CartUpdateService cartUpdateService;
    private final CartDeleteService cartDeleteService;

    // 장바구니 생성
    @PostMapping
    public ResponseEntity<CartResponseDto> createCart(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody CartCreateRequestDto requestDto
    ){
        Long userId = userDetails.getUser().getUserId();

        CartResponseDto responseDto = cartCreateService.createCart(userId, requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    // 장바구니 조회
    @GetMapping
    public ResponseEntity<List<CartResponseDto>> getCartList(
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ){
        Long userId = userDetails.getUserId();
        UserRoleEnum userRole = userDetails.getUser().getRole();

        if (userRole == UserRoleEnum.MANAGER || userRole == UserRoleEnum.MASTER) {
            List<CartResponseDto> orderList = cartGetListService.getCartListByAdmin();
            return ResponseEntity.ok(orderList);
        }
        List<CartResponseDto> orderList = cartGetListService.getCartList(userId);
        return ResponseEntity.ok(orderList);
    }

    // 장바구니 수정
    @PutMapping()
    public ResponseEntity<CartResponseDto> updateCart(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody CartUpdateRequestDto requestDto
    ){
        Long userId = userDetails.getUserId();
        UserRoleEnum userRole = userDetails.getUser().getRole();

        CartResponseDto responseDto = cartUpdateService.updateCart(userId, userRole, requestDto);
        return ResponseEntity.ok(responseDto);
    }

    // 장바구니 삭제
    @DeleteMapping("/{cartId}/delete")
    public ResponseEntity<CartResponseDto> deleteCart(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable UUID cartId
    ){
        Long userId = userDetails.getUserId();
        String userEmail = userDetails.getUsername();
        UserRoleEnum userRole = userDetails.getUser().getRole();

        if (userRole == UserRoleEnum.MANAGER || userRole == UserRoleEnum.MASTER) {
            CartResponseDto responseDto = cartDeleteService.deleteCartByAdmin(userEmail, cartId);
            return ResponseEntity.ok(responseDto);
        }
        CartResponseDto responseDto = cartDeleteService.deleteOrder(userId, cartId);
        return ResponseEntity.ok(responseDto);
    }
}
