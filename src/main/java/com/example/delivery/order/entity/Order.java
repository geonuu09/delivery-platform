package com.example.delivery.order.entity;

import com.example.delivery.cart.entity.Cart;
import com.example.delivery.common.entity.Timestamped;
import com.example.delivery.payment.entity.Payment;
import com.example.delivery.store.entity.Store;
import com.example.delivery.user.entity.User;
import jakarta.persistence.*;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "p_order")
@NoArgsConstructor
public class Order extends Timestamped {
    @Id
    @UuidGenerator
    @Column(name = "order_id", nullable = false)
    private UUID orderId;

    @PrePersist
    public void generateOrderId() {
        if (orderId == null) {
            orderId = UUID.randomUUID();
        }
    }

    @Column(nullable = false)
    private String dStreetAddress;

    @Column(nullable = false)
    private String dDetailAddress;

    @Column(nullable = false)
    private String requirements;

    @Column(nullable = false)
    private int totalCount;

    @Column(nullable = false)
    private int totalPrice;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @Getter
    public enum OrderStatus {
        RECEIVED("접수중"),
        PREPARING("준비중"),
        READY("준비완료"),
        IN_DELIVERY("배달중"),
        DELIVERED("배달완료");

        private final String label;
        OrderStatus(String label) {

            this.label = label;
        }
    }
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @OneToOne(mappedBy = "order", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Payment payment = new Payment();

    @OneToMany(mappedBy = "order", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Cart> carts = new ArrayList<Cart>();

}
