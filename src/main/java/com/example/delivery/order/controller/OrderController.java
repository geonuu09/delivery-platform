package com.example.delivery.order.controller;

import com.example.delivery.auth.security.UserDetailsImpl;
import com.example.delivery.order.dto.request.OrderCreateRequestDto;
import com.example.delivery.order.dto.response.OrderDetailResponseDto;
import com.example.delivery.order.dto.response.OrderListResponseDto;
import com.example.delivery.order.dto.response.OrderResponseDto;
import com.example.delivery.order.service.OrderService;
import com.example.delivery.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    // 주문 접수
    @PostMapping
    public ResponseEntity<OrderResponseDto> createOrder(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody OrderCreateRequestDto requestDto
    ){
        User user = userDetails.getUser();
        OrderResponseDto responseDto = orderService.createOrder(user, requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    // 주문 목록 조회
    @GetMapping
    public ResponseEntity<List<OrderListResponseDto>> getOrderList(
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ){
        Long userId = userDetails.getUser().getUserId();
        List<OrderListResponseDto> orderList = orderService.getOrderList(userId);
        return ResponseEntity.ok(orderList);
    }

    // 주문 상세내역 조회
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDetailResponseDto> getOrderDetail(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable UUID orderId
    ){
        Long userId = userDetails.getUser().getUserId();
        OrderDetailResponseDto responseDto = orderService.getOrderDetail(userId, orderId);
        return ResponseEntity.ok(responseDto);
    }

    // 주문 취소
    @PutMapping("/{orderId}/delete")
    public ResponseEntity<OrderResponseDto> deleteOrder(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long orderId
    ){
        return null;
    }

    // 주문 상태 변경
    @PutMapping("/{orderId}/{orderStatus}")
    public ResponseEntity<OrderResponseDto> updateOrderStatus(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long orderId,
            @PathVariable String orderStatus
    ){
        return null;
    }

}
