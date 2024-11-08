package com.example.delivery.order.entity;

import com.example.delivery.common.entity.Timestamped;
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
    @Column(name = "id", nullable = false)
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
}
