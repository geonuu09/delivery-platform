package com.example.delivery.order.entity;

import com.example.delivery.common.entity.Timestamped;
import jakarta.persistence.*;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "order")
@NoArgsConstructor
public class Orders extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private UUID orderId;

    @PrePersist
    public void generateOrderId() {
        if (orderId == null) {
            orderId = UUID.randomUUID();
        }
    }

//    @ManyToOne
//    @JoinColumn(name = "user_id", nullable = false)
//    private User user;

//    @ManyToOne
//    @JoinColumn(name = "store_id", nullable = false)
//    private Store store;

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
}
