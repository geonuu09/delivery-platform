package com.example.delivery.order.repository;

import com.example.delivery.order.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> , OrderRepositoryQuery {
    Page<Order> findAll(Pageable pageable);
    Page<Order> findByStore_StoreIdIn(List<UUID> storeIds, Pageable pageable);
    Page<Order> findByUser_UserId(Long userId, Pageable pageable);
}
