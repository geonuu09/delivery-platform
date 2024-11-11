package com.example.delivery.order.service;

import com.example.delivery.order.dto.request.OrderCreateRequestDto;
import com.example.delivery.order.dto.response.OrderListResponseDto;
import com.example.delivery.order.dto.response.OrderResponseDto;
import com.example.delivery.order.entity.Order;
import com.example.delivery.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;

    // 주문 접수
    public OrderResponseDto createOrder(OrderCreateRequestDto orderCreateRequestDto){
        UUID storeId = orderCreateRequestDto.getStoreId();

//        Store store = storeRepository.findById(storeId)
//                .orElseThrow(() -> new OrderException(ErrorCode.NOT_FOUND_STORE));

        Order order = orderCreateRequestDto.toEntity();
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

    // 주문 취소

    // 주문 상태 변경





}
