package com.example.delivery.order.controller;

import com.example.delivery.order.dto.request.OrderCreateRequestDto;
import com.example.delivery.order.dto.response.OrderListResponseDto;
import com.example.delivery.order.dto.response.OrderResponseDto;
import com.example.delivery.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    // 주문 접수
    @PostMapping
    public ResponseEntity<OrderResponseDto> createOrder(
            //@AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody OrderCreateRequestDto requestDto
    ){
        OrderResponseDto responseDto = orderService.createOrder(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    // 주문 목록 조회
    @GetMapping
    public ResponseEntity<List<OrderListResponseDto>> getOrderList(
            //@AuthenticationPrincipal UserDetailsImpl userDetails,
    ){
        Long userId = 1L;
        List<OrderListResponseDto> orderList = orderService.getOrderList(userId);
        return ResponseEntity.ok(orderList);
    }

    // 주문 내역 조회
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponseDto> getOrderDetail(
            //@AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long orderId
    ){

        return null;
    }

    // 주문 취소
    @PutMapping("/{orderId}/delete")
    public ResponseEntity<?> deleteOrder(
            //@AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long orderId
    ){
        return null;
    }

    // 주문 상태 변경
    @PutMapping("/{orderId}/{orderStatus}")
    public ResponseEntity<?> updateOrderStatus(
            //@AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long orderId,
            @PathVariable String orderStatus
    ){
        return null;
    }

}
