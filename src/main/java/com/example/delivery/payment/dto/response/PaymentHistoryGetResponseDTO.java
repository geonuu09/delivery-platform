package com.example.delivery.payment.dto.response;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class PaymentHistoryGetResponseDTO {
  private int amount;
  private LocalDateTime createdAt;
  private String storeName;
  private UUID paymentId;

}
