package com.example.delivery.order.controller;

import com.example.delivery.auth.security.UserDetailsImpl;
import com.example.delivery.order.dto.request.OrderCreateRequestDto;
import com.example.delivery.order.dto.response.OrderDetailResponseDto;
import com.example.delivery.order.dto.response.OrderListResponseDto;
import com.example.delivery.order.dto.response.OrderResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "Order API", description = "주문 관리 API")
public interface OrderControllerSwagger {

    @Operation(
            summary = "주문 접수",
            description = "새로운 주문을 접수합니다."
    )
    @PostMapping
    ResponseEntity<OrderResponseDto> createOrder(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody OrderCreateRequestDto requestDto);

    @Operation(
            summary = "주문 목록 조회",
            description = "주문 목록을 조회합니다."
    )
    @GetMapping
    ResponseEntity<Page<OrderListResponseDto>> getOrderList(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "false") boolean isAsc
    );

//    @Operation(
//            summary = "주문 목록 검색 조회",
//            description = "주문 목록을 검색하여 조회합니다."
//    )
//    @GetMapping("/search")
//    ResponseEntity<Page<OrderListResponseDto>> searchOrderList(
//            @AuthenticationPrincipal UserDetailsImpl userDetails,
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "10") int size,
//            @RequestParam(defaultValue = "createdAt") String sortBy,
//            @RequestParam(defaultValue = "false") boolean isAsc,
//            @RequestParam(required = false) String storeName,
//            @RequestParam(required = false) String menuName,
//            @RequestParam(required = false) String userEmail
//    );

    @Operation(
            summary = "주문 상세내역 조회",
            description = "주문 상세내역을 조회합니다."
    )
    @GetMapping("/{orderId}")
    ResponseEntity<OrderDetailResponseDto> getOrderDetail(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable UUID orderId
    );

    @Operation(
            summary = "주문 취소",
            description = "주문을 취소합니다."
    )
    @DeleteMapping("/{orderId}/delete")
    ResponseEntity<OrderResponseDto> deleteOrder(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable UUID orderId
    );

    @Operation(
            summary = "주문 상태 변경",
            description = "주문상태를 변경합니다."
    )
    @PutMapping("/{orderId}/{orderStatus}")
    @PreAuthorize("hasAnyRole('OWNER', 'MANAGER', 'MASTER')")
    ResponseEntity<OrderResponseDto> updateOrderStatus(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable UUID orderId,
            @PathVariable String orderStatus
    );
}
