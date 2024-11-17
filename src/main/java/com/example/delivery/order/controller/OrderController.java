package com.example.delivery.order.controller;

import com.example.delivery.auth.security.UserDetailsImpl;
import com.example.delivery.common.exception.CustomException;
import com.example.delivery.common.exception.code.ErrorCode;
import com.example.delivery.order.dto.request.OrderCreateRequestDto;
import com.example.delivery.order.dto.response.OrderDetailResponseDto;
import com.example.delivery.order.dto.response.OrderListResponseDto;
import com.example.delivery.order.dto.response.OrderResponseDto;
import com.example.delivery.order.service.OrderCreateService;
import com.example.delivery.order.service.OrderDeleteService;
import com.example.delivery.order.service.OrderGetService;
import com.example.delivery.order.service.OrderUpdateService;
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

    private final OrderCreateService orderCreateService;
    private final OrderGetService orderGetService;
    private final OrderUpdateService orderUpdateService;
    private final OrderDeleteService orderDeleteService;

    // 주문 접수
    @PostMapping
    public ResponseEntity<OrderResponseDto> createOrder(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody OrderCreateRequestDto requestDto
    ){
        User user = userDetails.getUser();
//        UserRoleEnum userRole = userDetails.getUser().getRole();

        Long userId = 1L;
        UserRoleEnum userRole = UserRoleEnum.CUSTOMER;

        if (userRole == UserRoleEnum.OWNER) {
            OrderResponseDto responseDto = orderCreateService.createOrderByOwner(userId, requestDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
        }
        OrderResponseDto responseDto = orderCreateService.createOrder(userId, requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    // 주문 목록 조회
    @GetMapping
    public ResponseEntity<Page<OrderListResponseDto>> getOrderList(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "false") boolean isAsc
    ){
//        Long userId = userDetails.getUser().getUserId();
//        UserRoleEnum userRole = userDetails.getUser().getRole();

        Long userId = 1L;
        UserRoleEnum userRole = UserRoleEnum.CUSTOMER;
        Page<OrderListResponseDto> orderList;

        if (userRole == UserRoleEnum.MANAGER || userRole == UserRoleEnum.MASTER) {
            orderList = orderGetService.getOrderListByAdmin(page, size, sortBy, isAsc);
        } else if (userRole == UserRoleEnum.OWNER){
            orderList = orderGetService.getOrderListByOwner(userId, page, size, sortBy, isAsc);
        } else {
        orderList = orderGetService.getOrderList(userId, page, size, sortBy, isAsc);
        }
        return ResponseEntity.ok(orderList);
    }

<<<<<<< Updated upstream
    // 주문 목록 검색 조회
=======
//    // 주문 목록 검색 조회
//    @Override
//    @GetMapping("/search")
//    public ResponseEntity<Page<OrderListResponseDto>> searchOrderList(
//            @AuthenticationPrincipal UserDetailsImpl userDetails,
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "10") int size,
//            @RequestParam(defaultValue = "createdAt") String sortBy,
//            @RequestParam(defaultValue = "false") boolean isAsc,
//            @RequestParam(required = false) String storeName,
//            @RequestParam(required = false) String menuName,
//            @RequestParam(required = false) String userEmail
//    ) {
////        Long userId = userDetails.getUser().getUserId();
////        UserRoleEnum userRole = userDetails.getUser().getRole();
//        Page<OrderListResponseDto> orderList;
//
//        Long userId = 1L;
//        UserRoleEnum userRole = UserRoleEnum.CUSTOMER;
//
//        if (userRole == UserRoleEnum.MANAGER || userRole == UserRoleEnum.MASTER) {
//            orderList = orderGetService.searchOrderListForAdmin(page, size, sortBy, isAsc, storeName, menuName, userEmail);
//        } else if (userRole == UserRoleEnum.OWNER) {
//            orderList = orderGetService.searchOrderListForOwner(userId, page, size, sortBy, isAsc, menuName, userEmail);
//        } else {
//            orderList = orderGetService.searchOrderListForCustomer(userId, page, size, sortBy, isAsc, storeName, menuName);
//        }
//        return ResponseEntity.ok(orderList);
//    }

>>>>>>> Stashed changes
    @GetMapping("/search")
    public ResponseEntity<Page<OrderListResponseDto>> searchOrderList(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "false") boolean isAsc,
            @RequestParam String keyword
    ) {
//        Long userId = userDetails.getUser().getUserId();
//        UserRoleEnum userRole = userDetails.getUser().getRole();
        Long userId = 1L;
        UserRoleEnum userRole = UserRoleEnum.CUSTOMER;

        Page<OrderListResponseDto> orderList = orderGetService.searchOrderListByKeyword(page, size, sortBy, isAsc, userId, userRole, keyword);
        return ResponseEntity.ok(orderList);
    }

    // 주문 상세내역 조회
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDetailResponseDto> getOrderDetail(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable UUID orderId
    ){
        Long userId = userDetails.getUser().getUserId();
        UserRoleEnum userRole = userDetails.getUser().getRole();

        if (userRole == UserRoleEnum.MANAGER || userRole == UserRoleEnum.MASTER) {
            OrderDetailResponseDto responseDto = orderGetService.getOrderDetailByAdmin(orderId);
            return ResponseEntity.ok(responseDto);
        }
        OrderDetailResponseDto responseDto = orderGetService.getOrderDetail(userId, orderId);
        return ResponseEntity.ok(responseDto);
    }

    // 주문 취소
    @DeleteMapping("/{orderId}/delete")
    public ResponseEntity<OrderResponseDto> deleteOrder(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable UUID orderId
    ){
        Long userId = userDetails.getUser().getUserId();
        String userEmail = userDetails.getUsername();
        UserRoleEnum userRole = userDetails.getUser().getRole();

        if (userRole == UserRoleEnum.MANAGER || userRole == UserRoleEnum.MASTER) {
            OrderResponseDto responseDto = orderDeleteService.deleteOrderByAdmin(userEmail, orderId);
            return ResponseEntity.ok(responseDto);
        }
        OrderResponseDto responseDto = orderDeleteService.deleteOrder(userId, orderId);
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
            throw new CustomException(ErrorCode. INVALID_PERMISSION);
        } else if (userRole == UserRoleEnum.MANAGER || userRole == UserRoleEnum.MASTER) {
            OrderResponseDto responseDto = orderUpdateService.updateOrderStatusByAdmin(orderId, orderStatus);
            return ResponseEntity.ok(responseDto);
        }
        OrderResponseDto responseDto = orderUpdateService.updateOrderStatus(userId, orderId, orderStatus);
        return ResponseEntity.ok(responseDto);
    }

}
