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

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartGetListService {

    private final CartRepository cartRepository;
    private final UserRepository userRepository;

    // 모든 장바구니 조회 : 관리자
    @Transactional
    public List<CartResponseDto> getCartListByAdmin() {
        List<Cart> cartList = cartRepository.findAll();
        return cartList.stream()
                .map(CartResponseDto::from)
                .collect(Collectors.toList());
    }

    // 장바구니 조회
    @Transactional
    public List<CartResponseDto> getCartList(Long userId) {
        // 현재 로그인 유저
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // 장바구니 목록 : 아직 주문이 안된 대기상태
        List<Cart> cartList = cartRepository.findByUser_UserIdAndCartStatus(userId, Cart.CartStatus.PENDING);

        return cartList.stream()
                .filter(cart -> user.getUserId().equals(cart.getUser().getUserId())) // 접근제한 : 주문자인지 확인
                .map(CartResponseDto::from)
                .collect(Collectors.toList());
    }
}
