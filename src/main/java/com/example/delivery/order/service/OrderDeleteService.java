package com.example.delivery.order.service;

import com.example.delivery.common.exception.CustomException;
import com.example.delivery.common.exception.code.ErrorCode;
import com.example.delivery.order.dto.response.OrderResponseDto;
import com.example.delivery.order.entity.Order;
import com.example.delivery.order.repository.OrderRepository;
import com.example.delivery.user.entity.User;
import com.example.delivery.user.entity.UserRoleEnum;
import com.example.delivery.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderDeleteService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    // 주문 취소 : 관리자
    @Transactional
    public OrderResponseDto deleteOrderByAdmin(String userEmail, UUID orderId) {
        // 해당 주문
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ORDER));

        // 시간 제한 : 주문 생성 후 5분 이내에만 취소 가능
        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.between(order.getCreatedAt(), now);

        if (duration.toMinutes() > 5) {
            throw new CustomException(ErrorCode.ORDER_CANCELLATION_TIMEOUT);
        }

        order.setOrderStatus(Order.OrderStatus.CANCELED);
        order.setDeletedAt(now);
        order.setDeletedBy(userEmail);

        return OrderResponseDto.from(order);
    }

    // 주문 취소
    @Transactional
    public OrderResponseDto deleteOrder(Long userId, UUID orderId) {
        // 현재 로그인 유저
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // 해당 주문
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ORDER));

        // 접근 제한
        checkOrderAccess(user, order);

        // 시간 제한 : 주문 생성 후 5분 이내에만 취소 가능
        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.between(order.getCreatedAt(), now);

        if (duration.toMinutes() > 5) {
            throw new CustomException(ErrorCode.ORDER_CANCELLATION_TIMEOUT);
        }

        order.setOrderStatus(Order.OrderStatus.CANCELED);
        order.setDeletedAt(now);
        order.setDeletedBy(user.getEmail());

        return OrderResponseDto.from(order);
    }

    // 접근 권한 확인
    private void checkOrderAccess(User user, Order order) {
        if (user.getRole() == UserRoleEnum.CUSTOMER) {
            // 주문자인지 확인
            if (!user.getUserId().equals(order.getUser().getUserId())) {
                throw new CustomException(ErrorCode.ORDER_PERMISSION_DENIED);
            }
        } else if (user.getRole() == UserRoleEnum.OWNER) {
            // 점주인지 확인
            if (!user.getUserId().equals(order.getStore().getUser().getUserId())) {
                throw new CustomException(ErrorCode.ORDER_PERMISSION_DENIED);
            }
        }
    }
}
