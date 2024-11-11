package com.example.delivery.cart.entity;


import com.example.delivery.common.entity.Timestamped;
import com.example.delivery.menu.entity.Menu;
import com.example.delivery.menu.entity.MenuOption;
import com.example.delivery.order.entity.Order;
import com.example.delivery.store.entity.Store;
import com.example.delivery.user.entity.User;
import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "p_cart")
@NoArgsConstructor
public class Cart extends Timestamped {
    @Id
    @UuidGenerator
    @Column(name = "cart_id", nullable = false)
    private UUID cartId;

    @Column(nullable = false)
    private int count;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CartStatus cartStatus;

    @Getter
    public enum CartStatus {
        PENDING("대기"),
        COMPLETED("완료"),
        DELETED("삭제");

        private final String label;
        CartStatus(String label) {

            this.label = label;
        }
    }
    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "menu_id", nullable = false)
    private Menu menu;

    @ManyToOne
    @JoinColumn(name = "menu_option_id", nullable = false)
    private MenuOption menuOption;
}
