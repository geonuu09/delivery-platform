package com.example.delivery.payment.entity;


import com.example.delivery.common.entity.Timestamped;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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




}
