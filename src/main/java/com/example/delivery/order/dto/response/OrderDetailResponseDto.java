package com.example.delivery.order.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class OrderDetailResponseDto {
    private UUID orderId;
    private String storeName;
    private String orderStatus;
//    private List<CartResponseDto> carts;
    private int totalCount;
    private int totalPrice;

    private String dStreetAddress;
    private String dDetailAddress;
    private String requirements;
//    private UserResponseDto user;
//    private StoreResponseDto store;
//    private PaymentResponseDto payment;

//
//    public static OrderDetailResponseDto from(Order order) {
//        return new OrderDetailResponseDto(
//                order.getOrderId(),
//                order.getDStreetAddress(),
//                order.getDDetailAddress(),
//                order.getRequirements(),
//                order.getTotalCount(),
//                order.getTotalPrice(),
//                order.getOrderStatus().getLabel(),  // Enum의 label을 String으로 변환
//                UserResponseDto.from(order.getUser()),  // User 엔터티를 DTO로 변환
//                StoreResponseDto.from(order.getStore()),  // Store 엔터티를 DTO로 변환
//                PaymentResponseDto.from(order.getPayment()),  // Payment 엔터티를 DTO로 변환
//                CartResponseDto.from(order.getCarts())  // Cart 엔터티들을 DTO로 변환
//        );
//    }
}

