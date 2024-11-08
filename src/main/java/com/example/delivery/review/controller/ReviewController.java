package com.example.delivery.review.controller;

import com.example.delivery.review.dto.request.ReviewRegisterRequestDTO;
import com.example.delivery.review.dto.response.ReviewShowResponseDTO;
import com.example.delivery.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/review")
public class ReviewController {

  private final ReviewService reviewService;

  // 리뷰 등록
  @PostMapping("/register")
  public ResponseEntity<?> reviewRegister(@Validated @RequestBody ReviewRegisterRequestDTO reviewRegisterDTO,
                                          UUID userId) {
    boolean result = reviewService.reviewRegister(reviewRegisterDTO, userId);
    return ResponseEntity.ok().body(result);
  }

  // 리뷰 조회
  @GetMapping("/show/{storeId}")
  public ResponseEntity<?> reviewShow(@PathVariable UUID storeId) {
    ReviewShowResponseDTO reviewShowResponseDTO = reviewService.reviewShow(storeId);
    return ResponseEntity.ok().body(reviewShowResponseDTO);
  }
}
