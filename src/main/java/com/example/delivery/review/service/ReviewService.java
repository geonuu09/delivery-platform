package com.example.delivery.review.service;

import com.example.delivery.review.dto.request.ReviewRegisterRequestDTO;
import com.example.delivery.review.dto.response.ReviewShowResponseDTO;
import com.example.delivery.review.entity.Review;
import com.example.delivery.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ReviewService {
  private final ReviewRepository reviewRepository;

  // 리뷰 등록
  public boolean reviewRegister(ReviewRegisterRequestDTO reviewRegisterDTO, UUID userId) {
    UUID uuid = UUID.randomUUID();
    Review entity = reviewRegisterDTO.toEntity();
    reviewRepository.save(entity);
    return true;
  }

  // 리뷰 조회
  public ReviewShowResponseDTO reviewShow(UUID storeId) {
    return null;
  }




}
