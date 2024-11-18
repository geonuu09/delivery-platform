package com.example.delivery.order.repository;

import com.example.delivery.order.entity.Order;
import com.example.delivery.user.entity.UserRoleEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderRepositoryQuery {
    Page<Order> searchOrderListByKeyword(Long userId, UserRoleEnum userRole, String keyword, Pageable pageable);

}
