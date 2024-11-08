package com.example.delivery.review.controller;

import com.example.delivery.review.dto.request.ReviewRegisterRequestDTO;
import com.example.delivery.review.service.ReviewService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

@SpringBootTest
class ReviewControllerTest {

  @Autowired
  private ReviewService reviewService;

  @Test
  @DisplayName("리뷰 등록 테스트")
  void reviewRegister() {
    // given
    UUID storeId = UUID.randomUUID(); // storeId에 적절한 UUID 값 설정
    ReviewRegisterRequestDTO reviewRegisterRequestDTO = ReviewRegisterRequestDTO.builder()
        .starRating(5)
        .content("리뷰 테스트")
        .build();

    // when
    boolean result = reviewService.reviewRegister(reviewRegisterRequestDTO, storeId);

    // then
    System.out.println("result = " + result);
  }
}
