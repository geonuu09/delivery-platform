package com.example.delivery.order.controller;

import com.example.delivery.auth.security.UserDetailsImpl;
import com.example.delivery.common.exception.CustomException;
import com.example.delivery.common.exception.code.ErrorCode;
import com.example.delivery.order.dto.request.OrderCreateRequestDto;
import com.example.delivery.order.dto.response.OrderDetailResponseDto;
import com.example.delivery.order.dto.response.OrderListResponseDto;
import com.example.delivery.order.dto.response.OrderResponseDto;
import com.example.delivery.order.service.OrderService;
import com.example.delivery.user.entity.User;
import com.example.delivery.user.entity.UserRoleEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    // 주문 접수
    @PostMapping
    @PreAuthorize("hasAnyRole('CUSTOMER','OWNER', 'MANAGER', 'MASTER')")
    public ResponseEntity<OrderResponseDto> createOrder(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody OrderCreateRequestDto requestDto
    ){
        User user = userDetails.getUser();
        UserRoleEnum userRole = userDetails.getUser().getRole();

        if (userRole == UserRoleEnum.OWNER) {
            OrderResponseDto responseDto = orderService.createOrderByOwner(user, requestDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
        }
        OrderResponseDto responseDto = orderService.createOrder(user, requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    // 주문 목록 조회
    @GetMapping
    @PreAuthorize("hasAnyRole('CUSTOMER','OWNER', 'MANAGER', 'MASTER')")
    public ResponseEntity<Page<OrderListResponseDto>> getOrderList(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "false") boolean isAsc
    ){
        Long userId = userDetails.getUser().getUserId();
        UserRoleEnum userRole = userDetails.getUser().getRole();
        Page<OrderListResponseDto> orderList;

        if (userRole == UserRoleEnum.MANAGER || userRole == UserRoleEnum.MASTER) {
            orderList = orderService.getOrderListByAdmin(page, size, sortBy, isAsc);
        } else if (userRole == UserRoleEnum.OWNER){
            orderList = orderService.getOrderListByOwner(userId, page, size, sortBy, isAsc);
        } else {
        orderList = orderService.getOrderList(userId, page, size, sortBy, isAsc);
        }
        return ResponseEntity.ok(orderList);
    }

    // 주문 목록 검색 조회
    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'OWNER', 'MANAGER', 'MASTER')")
    public ResponseEntity<Page<OrderListResponseDto>> searchOrderList(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "false") boolean isAsc,
            @RequestParam(required = false) String storeName,
            @RequestParam(required = false) String menuName,
            @RequestParam(required = false) String userEmail
    ) {
        Long userId = userDetails.getUser().getUserId();
        UserRoleEnum userRole = userDetails.getUser().getRole();
        Page<OrderListResponseDto> orderList;

        if (userRole == UserRoleEnum.MANAGER || userRole == UserRoleEnum.MASTER) {
            orderList = orderService.searchOrderListForAdmin(page, size, sortBy, isAsc, storeName, menuName, userEmail);
        } else if (userRole == UserRoleEnum.OWNER) {
            orderList = orderService.searchOrderListForOwner(userId, page, size, sortBy, isAsc, menuName, userEmail);
        } else {
            orderList = orderService.searchOrderListForCustomer(userId, page, size, sortBy, isAsc, storeName, menuName);
        }
        return ResponseEntity.ok(orderList);
    }

    // 주문 상세내역 조회
    @GetMapping("/{orderId}")
    @PreAuthorize("hasAnyRole('CUSTOMER','OWNER', 'MANAGER', 'MASTER')")
    public ResponseEntity<OrderDetailResponseDto> getOrderDetail(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable UUID orderId
    ){
        Long userId = userDetails.getUser().getUserId();
        UserRoleEnum userRole = userDetails.getUser().getRole();


        if (userRole == UserRoleEnum.MANAGER || userRole == UserRoleEnum.MASTER) {
            OrderDetailResponseDto responseDto = orderService.getOrderDetailByAdmin(orderId);
            return ResponseEntity.ok(responseDto);
        }
        OrderDetailResponseDto responseDto = orderService.getOrderDetail(userId, orderId);
        return ResponseEntity.ok(responseDto);
    }

    // 주문 취소
    @DeleteMapping("/{orderId}/delete")
    @PreAuthorize("hasAnyRole('CUSTOMER','OWNER', 'MANAGER', 'MASTER')")
    public ResponseEntity<OrderResponseDto> deleteOrder(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable UUID orderId
    ){
        Long userId = userDetails.getUser().getUserId();
        String userEmail = userDetails.getUsername();
        UserRoleEnum userRole = userDetails.getUser().getRole();

        if (userRole == UserRoleEnum.MANAGER || userRole == UserRoleEnum.MASTER) {
            OrderResponseDto responseDto = orderService.deleteOrderByAdmin(userEmail, orderId);
            return ResponseEntity.ok(responseDto);
        }
        OrderResponseDto responseDto = orderService.deleteOrder(userId, orderId);
        return ResponseEntity.ok(responseDto);
    }

    // 주문 상태 변경
    @PutMapping("/{orderId}/{orderStatus}")
    @PreAuthorize("hasAnyRole('OWNER', 'MANAGER', 'MASTER')")
    public ResponseEntity<OrderResponseDto> updateOrderStatus(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable UUID orderId,
            @PathVariable String orderStatus
    ){
        Long userId = userDetails.getUser().getUserId();
        UserRoleEnum userRole = userDetails.getUser().getRole();

        if(userRole == UserRoleEnum.CUSTOMER) {
            throw new CustomException(ErrorCode.UNAUTHORIZED, "해당 권한은 접근할 수 없습니다.");
        } else if (userRole == UserRoleEnum.MANAGER || userRole == UserRoleEnum.MASTER) {
            OrderResponseDto responseDto = orderService.updateOrderStatusByAdmin(orderId, orderStatus);
            return ResponseEntity.ok(responseDto);
        }
        OrderResponseDto responseDto = orderService.updateOrderStatus(userId, orderId, orderStatus);
        return ResponseEntity.ok(responseDto);
    }

}
