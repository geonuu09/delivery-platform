package com.example.delivery.cart.service;

import com.example.delivery.cart.dto.response.CartResponseDto;
import com.example.delivery.cart.entity.Cart;
import com.example.delivery.cart.repository.CartRepository;
import com.example.delivery.common.exception.CustomException;
import com.example.delivery.common.exception.code.ErrorCode;
import com.example.delivery.user.entity.User;
import com.example.delivery.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CartDeleteService {

    private final CartRepository cartRepository;
    private final UserRepository userRepository;

    // 장바구니 삭제 : 관리자
    @Transactional
    public CartResponseDto deleteCartByAdmin(String userEmail, UUID cartId) {
        // 해당 장바구니
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_CART));

        cart.setCartStatus(Cart.CartStatus.DELETED);

        // 로그
        cart.setDeletedAt(LocalDateTime.now());
        cart.setDeletedBy(userEmail);
        return CartResponseDto.from(cart);
    }

    // 장바구니 삭제
    @Transactional
    public CartResponseDto deleteOrder(Long userId, UUID cartId) {
        // 현재 로그인 유저
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // 해당 장바구니
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_CART));

        // 접근제한 : 주문자인지 확인
        if (!user.getUserId().equals(cart.getUser().getUserId())) {
            throw new CustomException(ErrorCode.CART_PERMISSION_DENIED);
        }

        // 장바구니 상태 체크 : 대기상태만 삭제 가능
        if (cart.getCartStatus() != Cart.CartStatus.PENDING) {
            throw new CustomException(ErrorCode.CART_DELETION_NOT_ALLOWED);
        }

        cart.setCartStatus(Cart.CartStatus.DELETED);

        // 로그
        cart.setDeletedAt(LocalDateTime.now());
        cart.setDeletedBy(user.getEmail());
        return CartResponseDto.from(cart);
    }
}
