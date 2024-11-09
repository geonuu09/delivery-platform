package com.example.delivery.payment.entity;


import com.example.delivery.common.entity.Timestamped;
import com.example.delivery.order.entity.Order;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@Table(name = "p_payment")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Payment extends Timestamped {

  @Id
  @UuidGenerator
  @Column(name = "payment_id")
  private UUID id;

  @Column(nullable = false)
  private int amount;

  @Column(name = "unique_payment_num")
  private String uniquePaymentNum;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "order_id", nullable = false)
  private Order order;



}
