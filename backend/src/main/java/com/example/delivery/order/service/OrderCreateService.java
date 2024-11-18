package com.example.delivery.order.service;

import com.example.delivery.cart.entity.Cart;
import com.example.delivery.cart.repository.CartRepository;
import com.example.delivery.common.exception.CustomException;
import com.example.delivery.common.exception.code.ErrorCode;
import com.example.delivery.order.dto.request.OrderCreateRequestDto;
import com.example.delivery.order.dto.response.OrderResponseDto;
import com.example.delivery.order.entity.Order;
import com.example.delivery.order.repository.OrderRepository;
import com.example.delivery.store.entity.Store;
import com.example.delivery.store.repository.StoreRepository;
import com.example.delivery.user.entity.User;
import com.example.delivery.user.entity.UserRoleEnum;
import com.example.delivery.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderCreateService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final StoreRepository storeRepository;
    private final CartRepository cartRepository;


    // 주문 접수
    @Transactional
    public OrderResponseDto createOrder(Long userId, OrderCreateRequestDto requestDto){
        // 현재 로그인 유저
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        UserRoleEnum userRole = user.getRole();

        // 주문 가게
        UUID storeId = requestDto.getStoreId();

        Store store = storeRepository.findById(storeId)
                 .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_STORE));

        // 장바구니 확인
        List<Cart> cartList = cartRepository.findByCartStatus(Cart.CartStatus.PENDING);

        if (cartList.isEmpty()) {
            throw new CustomException(ErrorCode.NOT_FOUND_CART);
        }

        // 권한 확인
        if (userRole == UserRoleEnum.OWNER) {
            // 점주인지 확인
            if (!user.getUserId().equals(store.getUser().getUserId())) {
                throw new CustomException(ErrorCode.ORDER_PERMISSION_DENIED);
            }
        }

        // 주문
        Order order = requestDto.toEntity(user, store);

        int totalCount = 0;
        int totalPrice = 0;

        // 총 수량, 총 가격 계산
        for (Cart cart : cartList) {
            totalCount += cart.getCount();
            totalPrice += cart.getPrice();

            // 장바구니 저장
            cart.setCartStatus(Cart.CartStatus.COMPLETED);
            cart.setOrder(order);
            cartRepository.save(cart);
        }

        // 총 수량, 총 가격 설정
        order.setTotalCount(totalCount);
        order.setTotalPrice(totalPrice);

        orderRepository.save(order);
        return OrderResponseDto.from(order);
    }
}
