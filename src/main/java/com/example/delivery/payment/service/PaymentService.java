package com.example.delivery.payment.service;

import com.example.delivery.auth.security.UserDetailsImpl;
import com.example.delivery.common.Util.PagingUtil;
import com.example.delivery.common.exception.CustomException;
import com.example.delivery.common.exception.code.ErrorCode;
import com.example.delivery.order.entity.Order;
import com.example.delivery.order.repository.OrderRepository;
import com.example.delivery.payment.dto.request.PaymentRegisterRequestDTO;
import com.example.delivery.payment.dto.response.PaymentHistoryDetailGetResponseDTO;
import com.example.delivery.payment.dto.response.PaymentHistoryGetResponseDTO;
import com.example.delivery.payment.entity.Payment;
import com.example.delivery.payment.repository.PaymentRepository;
import com.example.delivery.user.entity.User;
import com.example.delivery.user.entity.UserRoleEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {
  private final PaymentRepository paymentRepository;
  private final OrderRepository orderRepository;

  // 결제 등록
  public boolean registerPayment(UserDetailsImpl userDetails, PaymentRegisterRequestDTO paymentRegisterRequestDTO) {
    User user = userDetails.getUser();
    UUID orderId = paymentRegisterRequestDTO.getOrderId();
    Order order = orderRepository.findById(orderId)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ORDER));
    Payment payment = Payment.builder()
        .uniquePaymentNum(paymentRegisterRequestDTO.getUniquePaymentNum())
        .amount(order.getTotalPrice())
        .order(order)
        .build();
    paymentRepository.save(payment);
    return true;
  }

  // 결제 내역
  public Page<PaymentHistoryGetResponseDTO> getPaymentHistory(UserDetailsImpl userDetails, int page, int size, String sortBy, boolean isAsc) {
    Pageable pageable = PagingUtil.createPageable(page, size, isAsc, sortBy);
    User user = userDetails.getUser();

    // 점주, 회원
    if (!userDetails.getUser().getRole().equals(UserRoleEnum.MASTER) || userDetails.getUser().getRole().equals(UserRoleEnum.MANAGER)) {
      Page<Payment> payments = paymentRepository.findByOrder_User(user, pageable);
      return payments.map(payment ->
          PaymentHistoryGetResponseDTO.builder()
              .amount(payment.getAmount())
              .createdAt(payment.getCreatedAt())
              .storeName(payment.getOrder().getStore().getStoreName())
              .paymentId(payment.getId())
              .build());
    }else { // 관리자
      Page<Payment> payments = paymentRepository.findAll(pageable);
      return payments.map(payment ->
          PaymentHistoryGetResponseDTO.builder()
              .amount(payment.getAmount())
              .createdAt(payment.getCreatedAt())
              .storeName(payment.getOrder().getStore().getStoreName())
              .paymentId(payment.getId())
              .build());
    }
  }

  // 결제 상세 내역
  public PaymentHistoryDetailGetResponseDTO getPaymentHistoryDetail(UserDetailsImpl userDetails, UUID paymentId) {
    User user = userDetails.getUser();
    Payment payment = paymentRepository.findById(paymentId)
        .orElseThrow(() -> new CustomException(ErrorCode.PAYMENT_NOT_FOUND));
    return new PaymentHistoryDetailGetResponseDTO(payment, payment.getOrder().getStore());

  }
}
