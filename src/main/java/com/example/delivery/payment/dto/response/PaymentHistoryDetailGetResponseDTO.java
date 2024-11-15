package com.example.delivery.payment.dto.response;

import com.example.delivery.payment.entity.Payment;
import com.example.delivery.store.entity.Store;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class PaymentHistoryDetailGetResponseDTO {

  private String storeName;
  private String storeOwnerName;
  private String storeLocation;
  private int amount;
  private String uniquePaymentNum;

  public PaymentHistoryDetailGetResponseDTO(Payment payment, Store store) {
    this.amount = payment.getAmount();
    this.uniquePaymentNum = payment.getUniquePaymentNum();
    this.storeOwnerName = store.getStoreOwnerName();
    this.storeLocation = store.getStoreLocation();
    this.storeName = store.getStoreName();
  }
}
