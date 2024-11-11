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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final UserRepository userRepository;
    private final StoreRepository storeRepository;
    private final OrderRepository orderRepository;

    // 주문 접수
    public OrderResponseDto createOrder(User user, OrderCreateRequestDto orderCreateRequestDto){
        UUID storeId = orderCreateRequestDto.getStoreId();

        Store store = storeRepository.findById(storeId)
                 .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_STORE));

        Order order = orderCreateRequestDto.toEntity(user, store);
        orderRepository.save(order);
        return OrderResponseDto.from(order);
    }

    // 주문 목록 조회
    public List<OrderListResponseDto> getOrderList(Long userId) {
        List<Order> orderList = orderRepository.findByUserId(userId);
        return orderList.stream()
                .map(OrderListResponseDto::from)
                .collect(Collectors.toList());
    }

    // 주문 상세내역 조회
    public OrderDetailResponseDto getOrderDetail(Long userId, UUID orderId) {
        // 현재 로그인 유저
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // 해당 주문
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ORDER));

        // 접근 제한
        if (user.getRole() == UserRoleEnum.CUSTOMER) {
            // 주문자인지 확인
            if (!userId.equals(order.getUser().getUserId())) {
                throw new CustomException(ErrorCode.ORDER_PERMISSION_DENIED);
            }
        } else if (user.getRole() == UserRoleEnum.OWNER) {
            // 점주인지 확인
            if (!userId.equals(order.getStore().getUser().getUserId())) {
                throw new CustomException(ErrorCode.ORDER_PERMISSION_DENIED);
            }
        }
        return OrderDetailResponseDto.from(order);
    }

    // 주문 취소


    // 주문 상태 변경


}
