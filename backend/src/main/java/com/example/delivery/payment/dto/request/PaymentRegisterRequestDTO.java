package com.example.delivery.payment.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class PaymentRegisterRequestDTO {

  @NotNull
  private UUID orderId;
  @NotNull
  private String uniquePaymentNum;
}
