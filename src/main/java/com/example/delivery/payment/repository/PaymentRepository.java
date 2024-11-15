package com.example.delivery.payment.repository;

import com.example.delivery.payment.entity.Payment;
import com.example.delivery.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PaymentRepository extends JpaRepository<Payment, UUID> {
  Page<Payment> findByUser(User user, Pageable pageable);
}
