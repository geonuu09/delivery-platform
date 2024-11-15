package com.example.delivery.order.service;

import com.example.delivery.cart.entity.Cart;
import com.example.delivery.cart.repository.CartRepository;
import com.example.delivery.common.Util.PagingUtil;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    private final CartRepository cartRepository;


    // 주문 접수
    @Transactional
    public OrderResponseDto createOrder(Long userId, OrderCreateRequestDto requestDto){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        UUID storeId = requestDto.getStoreId();

        Store store = storeRepository.findById(storeId)
                 .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_STORE));

        Order order = requestDto.toEntity(user, store);

        orderRepository.save(order);

        // 장바구니
        List<Cart> cartList = cartRepository.findByCartStatus(Cart.CartStatus.PENDING);

        if (cartList.isEmpty()) {
            throw new CustomException(ErrorCode.NOT_FOUND_CART);
        }

        for (Cart cart : cartList) {
            cart.setCartStatus(Cart.CartStatus.COMPLETED);
            cart.setOrder(order);
            cartRepository.save(cart);
        }
        return OrderResponseDto.from(order);
    }

    // 주문 접수
    @Transactional
    public OrderResponseDto createOrderByOwner(User user, OrderCreateRequestDto requestDto) {
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        UUID storeId = requestDto.getStoreId();

        // 해당 가게
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_STORE));

        // 점주인지 확인
        if (!user.getUserId().equals(store.getUser().getUserId())) {
            throw new CustomException(ErrorCode.ORDER_PERMISSION_DENIED);
        }

        Order order = requestDto.toEntity(user, store);
        orderRepository.save(order);

        // 장바구니
        List<Cart> cartList = cartRepository.findByCartStatus(Cart.CartStatus.PENDING);

        if (cartList.isEmpty()) {
            throw new CustomException(ErrorCode.NOT_FOUND_CART);
        }

        for (Cart cart : cartList) {
            cart.setCartStatus(Cart.CartStatus.COMPLETED);
            cart.setOrder(order);
            cartRepository.save(cart);
        }
        return OrderResponseDto.from(order);
    }

    // 주문 목록 조회 : 관리자
    @Transactional
    public Page<OrderListResponseDto> getOrderListByAdmin(int page, int size, String sortBy, boolean isAsc) {
        Pageable pageable = PagingUtil.createPageable(page, size, isAsc, sortBy);

        Page<Order> orderPage = orderRepository.findAll(pageable);
        return orderPage.map(OrderListResponseDto::from);
    }

    // 주문 목록 조회 : 점주
    @Transactional
    public Page<OrderListResponseDto> getOrderListByOwner(Long userId, int page, int size, String sortBy, boolean isAsc) {
        Pageable pageable = PagingUtil.createPageable(page, size, isAsc, sortBy);

        // 현재 로그인 유저
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // 해당 점주의 모든 가게
        List<Store> storeList = storeRepository.findByUser_UserId(user.getUserId());
        if (storeList.isEmpty()) {
            throw new CustomException(ErrorCode.NOT_FOUND_STORE);
        }

        List<UUID> storeIdList = storeList.stream()
                .map(Store::getStoreId)
                .collect(Collectors.toList());

        // 점주 가게들의 주문 목록
        Page<Order> orderPage = orderRepository.findByStore_StoreIdIn(storeIdList, pageable);
        return orderPage.map(OrderListResponseDto::from);
    }

    // 주문 목록 조회
    @Transactional
    public List<OrderListResponseDto> getOrderList(Long userId) {
        List<Order> orderList = orderRepository.findAll();
        return orderList.stream()
                .map(OrderListResponseDto::from)
                .collect(Collectors.toList());
    }

    public Page<OrderListResponseDto> getOrderList(Long userId, int page, int size, String sortBy, boolean isAsc) {
        Pageable pageable = PagingUtil.createPageable(page, size, isAsc, sortBy);

        // 고객의 주문 목록
        Page<Order> orderPage = orderRepository.findByUser_UserId(userId, pageable);
        return orderPage.map(OrderListResponseDto::from);
    }

    // 관리자용 주문 목록 검색 조회 : 주문자,가게명, 주문메뉴
    public Page<OrderListResponseDto> searchOrderListForAdmin(int page, int size, String sortBy, boolean isAsc,
                                                              String storeName, String menuName, String userEmail) {
        Pageable pageable = PagingUtil.createPageable(page, size, isAsc, sortBy);

        Page<Order> orderPage = orderRepository.searchOrderListForAdmin(storeName, menuName, userEmail ,pageable);
        return orderPage.map(OrderListResponseDto::from);
    }

    // 점주용 주문 목록 검색 조회 : 주문자, 주문메뉴
    public Page<OrderListResponseDto> searchOrderListForOwner(Long userId, int page, int size, String sortBy, boolean isAsc,
                                                              String menuName, String userEmail) {
        Pageable pageable = PagingUtil.createPageable(page, size, isAsc, sortBy);

        Page<Order> orderPage = orderRepository.searchOrderListForOwner(userId,menuName,userEmail,pageable);
        return orderPage.map(OrderListResponseDto::from);
    }

    // 고객용 주문 목록 검색 조회 : 가게명, 주문메뉴
    public Page<OrderListResponseDto> searchOrderListForCustomer(Long userId, int page, int size, String sortBy, boolean isAsc,
                                                                 String storeName, String menuName) {
        Pageable pageable = PagingUtil.createPageable(page, size, isAsc, sortBy);

        Page<Order> orderPage = orderRepository.searchOrderListForCustomer(userId, storeName, menuName,pageable);
        return orderPage.map(OrderListResponseDto::from);
    }

    // 주문 상세내역 조회 : 관리자
    @Transactional
    public OrderDetailResponseDto getOrderDetailByAdmin(UUID orderId) {
        // 해당 주문
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ORDER));
        return OrderDetailResponseDto.from(order);
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

    // 주문 상태 변경 : 관리자
    @Transactional
    public OrderResponseDto updateOrderStatusByAdmin(UUID orderId, String orderStatus) {
        // 해당 주문
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ORDER));

        Order.OrderStatus status = Order.OrderStatus.valueOf(orderStatus);
        order.setOrderStatus(status);

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

        // OWNER라면 점주인지 확인
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
