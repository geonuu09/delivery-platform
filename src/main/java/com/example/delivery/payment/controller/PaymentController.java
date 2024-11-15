package com.example.delivery.payment.controller;

import com.example.delivery.auth.security.UserDetailsImpl;
import com.example.delivery.payment.dto.request.PaymentRegisterRequestDTO;
import com.example.delivery.payment.dto.response.PaymentHistoryDetailGetResponseDTO;
import com.example.delivery.payment.dto.response.PaymentHistoryGetResponseDTO;
import com.example.delivery.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/payments")
public class PaymentController {
  private final PaymentService paymentService;

  // 결제 등록
  @PostMapping
  @PreAuthorize("hasRole('CUSTOMER')")
  public ResponseEntity<?> registerPayment(
      @AuthenticationPrincipal UserDetailsImpl userDetails,
      @RequestBody PaymentRegisterRequestDTO paymentRegisterRequestDTO

  ) {
    boolean result = paymentService.registerPayment(userDetails, paymentRegisterRequestDTO);
    return ResponseEntity.ok().body(result);
  }

  // 결제 내역
  @GetMapping("/history")
  @PreAuthorize("hasAnyRole('OWNER', 'CUSTOMER', 'MANAGER', 'MASTER')")
  public ResponseEntity<?> getPaymentHistory(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                             @RequestParam("page") int page,
                                             @RequestParam("size") int size,
                                             @RequestParam("sortBy") String sortBy,
                                             @RequestParam("isAsc") boolean isAsc) {
    Page<PaymentHistoryGetResponseDTO> paymentHistory = paymentService.getPaymentHistory(userDetails, page - 1, size, sortBy, isAsc);
    return ResponseEntity.ok().body(paymentHistory);
  }

  // 결제 상세 내역
  @GetMapping("/{paymentId}/history/detail")
  @PreAuthorize("hasAnyRole('OWNER', 'CUSTOMER', 'MANAGER', 'MASTER')")
  public ResponseEntity<?> getPaymentHistoryDetail(@PathVariable("paymentId") UUID paymentId,
                                                   @AuthenticationPrincipal UserDetailsImpl userDetails) {
    PaymentHistoryDetailGetResponseDTO paymentHistoryDetail = paymentService.getPaymentHistoryDetail(userDetails, paymentId);
    return ResponseEntity.ok().body(paymentHistoryDetail);
  }
}
