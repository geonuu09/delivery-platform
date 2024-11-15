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
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        UUID storeId = requestDto.getStoreId();

        Store store = storeRepository.findById(storeId)
                 .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_STORE));

        Order order = requestDto.toEntity(user, store);

        orderRepository.save(order);

        // 장바구니
        List<Cart> cartList = cartRepository.findByCartStatus(Cart.CartStatus.PENDING);

        if (cartList.isEmpty()) {
            throw new CustomException(ErrorCode.NOT_FOUND_CART);
        }

        for (Cart cart : cartList) {
            cart.setCartStatus(Cart.CartStatus.COMPLETED);
            cart.setOrder(order);
            cartRepository.save(cart);
        }
        return OrderResponseDto.from(order);
    }

    // 주문 접수 : 점주
    @Transactional
    public OrderResponseDto createOrderByOwner(User user, OrderCreateRequestDto requestDto) {
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        UUID storeId = requestDto.getStoreId();

        // 해당 가게
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_STORE));

        // 점주인지 확인
        if (!user.getUserId().equals(store.getUser().getUserId())) {
            throw new CustomException(ErrorCode.ORDER_PERMISSION_DENIED);
        }

        Order order = requestDto.toEntity(user, store);
        orderRepository.save(order);

        // 장바구니
        List<Cart> cartList = cartRepository.findByCartStatus(Cart.CartStatus.PENDING);

        if (cartList.isEmpty()) {
            throw new CustomException(ErrorCode.NOT_FOUND_CART);
        }

        for (Cart cart : cartList) {
            cart.setCartStatus(Cart.CartStatus.COMPLETED);
            cart.setOrder(order);
            cartRepository.save(cart);
        }
        return OrderResponseDto.from(order);
    }
}
