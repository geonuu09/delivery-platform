package com.example.delivery.order.service;

import com.example.delivery.common.exception.CustomException;
import com.example.delivery.common.exception.code.ErrorCode;
import com.example.delivery.order.dto.response.OrderResponseDto;
import com.example.delivery.order.entity.Order;
import com.example.delivery.order.repository.OrderRepository;
import com.example.delivery.user.entity.User;
import com.example.delivery.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderUpdateService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    // 주문 상태 변경 : 관리자
    @Transactional
    public OrderResponseDto updateOrderStatusByAdmin(UUID orderId, String orderStatus) {
        // 해당 주문
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ORDER));

        Order.OrderStatus status = Order.OrderStatus.valueOf(orderStatus);
        order.setOrderStatus(status);

        return OrderResponseDto.from(order);
    }

    // 주문 상태 변경
    @Transactional
    public OrderResponseDto updateOrderStatus(Long userId, UUID orderId, String orderStatus) {
        // 현재 로그인 유저
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // 해당 주문
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ORDER));

        // OWNER라면 점주인지 확인
        if (!user.getUserId().equals(order.getStore().getUser().getUserId())) {
            throw new CustomException(ErrorCode.ORDER_PERMISSION_DENIED);
        }

        Order.OrderStatus status = Order.OrderStatus.valueOf(orderStatus);
        order.setOrderStatus(status);

        return OrderResponseDto.from(order);
    }
}
