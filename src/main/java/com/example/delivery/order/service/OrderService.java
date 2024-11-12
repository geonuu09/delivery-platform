package com.example.delivery.order.service;

import com.example.delivery.common.exception.CustomException;
import com.example.delivery.common.exception.code.ErrorCode;
import com.example.delivery.order.dto.request.OrderCreateRequestDto;
import com.example.delivery.order.dto.response.OrderDetailResponseDto;
import com.example.delivery.order.dto.response.OrderListResponseDto;
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

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final StoreRepository storeRepository;

    // 주문 접수
    @Transactional
    public OrderResponseDto createOrder(User user, OrderCreateRequestDto orderCreateRequestDto){
        UUID storeId = orderCreateRequestDto.getStoreId();

        Store store = storeRepository.findById(storeId)
                 .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_STORE));

        Order order = orderCreateRequestDto.toEntity(user, store);
        orderRepository.save(order);
        return OrderResponseDto.from(order);
    }

    // 주문 목록 조회
    @Transactional
    public List<OrderListResponseDto> getOrderList(Long userId) {
        List<Order> orderList = orderRepository.findAll();
        return orderList.stream()
                .map(OrderListResponseDto::from)
                .collect(Collectors.toList());
    }

    // 주문 상세내역 조회
    @Transactional
    public OrderDetailResponseDto getOrderDetail(Long userId, UUID orderId) {
        // 현재 로그인 유저
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // 해당 주문
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ORDER));

        // 접근 제한
        checkOrderAccess(user, order);

        return OrderDetailResponseDto.from(order);
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
        order.setDeletedBy(user.getUserName());

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

        // 점주인지 확인
        if (!user.getUserId().equals(order.getStore().getUser().getUserId())) {
            throw new CustomException(ErrorCode.ORDER_PERMISSION_DENIED);
        }

        Order.OrderStatus status = Order.OrderStatus.valueOf(orderStatus);
        order.setOrderStatus(status);

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
