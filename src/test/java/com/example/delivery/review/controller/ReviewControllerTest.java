package com.example.delivery.review.controller;

import com.example.delivery.review.dto.request.ReviewRegisterRequestDTO;
import com.example.delivery.review.repository.ReviewRepository;
import com.example.delivery.review.service.ReviewService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
class ReviewControllerTest {

  @InjectMocks
  private ReviewService reviewService;

  @Mock
  private ReviewRepository reviewRepository;

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
    boolean result = reviewService.reviewRegister(reviewRegisterRequestDTO);
    // then
    assertTrue(result);

  }
}
