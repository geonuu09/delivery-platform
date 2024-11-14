package com.example.delivery.cart.repository;

import com.example.delivery.cart.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CartRepository extends JpaRepository<Cart, UUID> {
    List<Cart> findByUser_UserIdAndCartStatus(Long userId, Cart.CartStatus cartStatus);

    List<Cart> findByCartStatus(Cart.CartStatus cartStatus);
}
