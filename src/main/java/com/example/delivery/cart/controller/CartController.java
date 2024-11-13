package com.example.delivery.cart.controller;

import com.example.delivery.auth.security.UserDetailsImpl;
import com.example.delivery.cart.dto.request.CartCreateRequestDto;
import com.example.delivery.cart.dto.request.CartUpdateRequestDto;
import com.example.delivery.cart.dto.response.CartResponseDto;
import com.example.delivery.cart.service.CartService;
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

    private final CartService cartService;

    // 장바구니 생성
    @PostMapping
    //@PreAuthorize("hasAnyRole('CUSTOMER','OWNER', 'MANAGER', 'MASTER')")
    public ResponseEntity<CartResponseDto> createCart(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody CartCreateRequestDto requestDto
    ){
        //User user = userDetails.getUser();
        Long userId = 1L;
        CartResponseDto responseDto = cartService.createCart(userId, requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    // 장바구니 조회
    @GetMapping
    //@PreAuthorize("hasAnyRole('CUSTOMER','OWNER', 'MANAGER', 'MASTER')")
    public ResponseEntity<List<CartResponseDto>> getCartList(
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ){
        //Long userId = userDetails.getUserId();
        //UserRoleEnum userRole = userDetails.getUser().getRole();
        Long userId = 1L;
        UserRoleEnum userRole = UserRoleEnum.CUSTOMER;

        if (userRole == UserRoleEnum.MANAGER || userRole == UserRoleEnum.MASTER) {
            List<CartResponseDto> orderList = cartService.getCartListByAdmin();
            return ResponseEntity.ok(orderList);
        }
        List<CartResponseDto> orderList = cartService.getCartList(userId);
        return ResponseEntity.ok(orderList);
    }

    // 장바구니 수정
    @PutMapping()
    //@PreAuthorize("hasAnyRole('CUSTOMER','OWNER', 'MANAGER', 'MASTER')")
    public ResponseEntity<CartResponseDto> updateCart(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody CartUpdateRequestDto requestDto

    ){
        //Long userId = userDetails.getUserId();
        //UserRoleEnum userRole = userDetails.getUser().getRole();
        Long userId = 1L;
        UserRoleEnum userRole = UserRoleEnum.CUSTOMER;

        if (userRole == UserRoleEnum.MANAGER || userRole == UserRoleEnum.MASTER) {
            CartResponseDto ResponseDto = cartService.updateCartByAdmin(requestDto);
            return ResponseEntity.ok(ResponseDto);
        }
        CartResponseDto responseDto = cartService.updateCart(userId,requestDto);
        return ResponseEntity.ok(responseDto);
    }

    // 장바구니 삭제
    @DeleteMapping("/{cartId}/delete")
    //@PreAuthorize("hasAnyRole('CUSTOMER','OWNER', 'MANAGER', 'MASTER')")
    public ResponseEntity<CartResponseDto> deleteCart(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable UUID cartId
    ){
        //Long userId = userDetails.getUserId();
        //String userEmail = userDetails.getUsername();
        //UserRoleEnum userRole = userDetails.getUser().getRole();
        Long userId = 1L;
        String userEmail = "customer@test.com";
        UserRoleEnum userRole = UserRoleEnum.CUSTOMER;

        if (userRole == UserRoleEnum.MANAGER || userRole == UserRoleEnum.MASTER) {
            CartResponseDto responseDto = cartService.deleteCartByAdmin(userEmail, cartId);
            return ResponseEntity.ok(responseDto);
        }
        CartResponseDto responseDto = cartService.deleteOrder(userId, cartId);
        return ResponseEntity.ok(responseDto);
    }
}
