package com.example.delivery.order.repository;

import com.example.delivery.order.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {
    Page<Order> findAll(Pageable pageable);
    Page<Order> findByStore_StoreIdIn(List<UUID> storeIds, Pageable pageable);
    Page<Order> findByUser_UserId(Long userId, Pageable pageable);

    @Query("SELECT o FROM Order o " +
            "JOIN o.store s " +
            "JOIN o.user u " +
            "JOIN o.carts c " +
            "JOIN c.menu m " +
            "WHERE (:storeName IS NULL OR s.storeName LIKE %:storeName%) " +
            "AND (:menuName IS NULL OR m.menuName LIKE %:menuName%) " +
            "AND (:userEmail IS NULL OR u.email LIKE %:userEmail%)")
    Page<Order> searchOrderListForAdmin(
            @Param("storeName") String storeName,
            @Param("menuName") String menuName,
            @Param("customerName") String customerName,
            Pageable pageable);

    @Query("SELECT o FROM Order o " +
            "JOIN o.store s " +
            "JOIN s.user u " +
            "JOIN o.carts c " +
            "JOIN c.menu m " +
            "WHERE u.userId = :userId " +
            "AND (:menuName IS NULL OR m.menuName LIKE %:menuName%)" +
            "AND (:userEmail IS NULL OR o.user.email LIKE %:userEmail%)")
    Page<Order> searchOrderListForOwner(
            @Param("userId") Long userId,
            @Param("menuName") String menuName,
            @Param("userEmail") String userEmail,
            Pageable pageable);

    @Query("SELECT o FROM Order o " +
            "JOIN o.store s " +
            "JOIN o.carts c " +
            "JOIN c.menu m " +
            "WHERE o.user.userId = :userId " +
            "AND (:storeName IS NULL OR s.storeName LIKE %:storeName%) " +
            "AND (:menuName IS NULL OR m.menuName LIKE %:menuName%)")
    Page<Order> searchOrderListForCustomer(
            @Param("userId") Long userId,
            @Param("storeName") String storeName,
            @Param("menuName") String menuName,
            Pageable pageable);
}
