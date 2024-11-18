package com.example.delivery.review.service;

import com.example.delivery.review.dto.request.ReviewRegisterRequestDTO;
import com.example.delivery.user.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@Commit
class ReviewServiceTest {
  @Autowired
  private ReviewService reviewService;

  @PersistenceContext
  private EntityManager entityManager;
  @Test
  @DisplayName("리뷰 등록")
  void createReview() {
    // given
    UUID uuid = UUID.randomUUID();
    ReviewRegisterRequestDTO reviewRegisterRequestDTO = ReviewRegisterRequestDTO.builder()
        .content("리뷰내용")
        .starRating(5)
        .storeId(uuid)
        .build();


  }
}
