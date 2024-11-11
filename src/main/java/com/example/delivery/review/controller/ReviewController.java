package com.example.delivery.review.controller;

import com.example.delivery.auth.security.UserDetailsImpl;
import com.example.delivery.review.dto.request.ReviewEditRequestDTO;
import com.example.delivery.review.dto.request.ReviewRegisterRequestDTO;
import com.example.delivery.review.dto.response.ReviewEditResponseDTO;
import com.example.delivery.review.dto.response.ReviewListResponseDTO;
import com.example.delivery.review.dto.response.ReviewShowResponseDTO;
import com.example.delivery.review.service.ReviewService;
import com.example.delivery.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/reviews")
public class ReviewController {

  private final ReviewService reviewService;

  // 리뷰 등록
  @PostMapping("/register")
  @PreAuthorize("hasAnyRole('OWNER', 'CUSTOMER')")
  public ResponseEntity<?> reviewRegister(@RequestPart("review") @Validated ReviewRegisterRequestDTO reviewRegisterRequestDTO,
                                          @RequestPart(value = "reviewImage", required = false) MultipartFile reviewImage,
                                          @AuthenticationPrincipal UserDetailsImpl user
                                          ) {
    boolean result = reviewService.reviewRegister(reviewRegisterRequestDTO, user, reviewImage);
    return ResponseEntity.ok().body(result);
  }

  // 리뷰 전체 조회
  @GetMapping("/show/{storeId}")
  @PreAuthorize("hasAnyRole('OWNER', 'CUSTOMER', 'MANAGER', 'MASTER')")
  public ResponseEntity<?> reviewShow(@PathVariable UUID storeId,
                                      @RequestParam("page") int page,
                                      @RequestParam("size") int size,
                                      @RequestParam("sortBy") String sortBy,
                                      @RequestParam("isAsc") boolean isAsc,
                                      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    Page<ReviewShowResponseDTO> reviewShowResponseDTO = reviewService.reviewShow(storeId, page - 1, size, sortBy, isAsc, userDetails);
    return ResponseEntity.ok().body(reviewShowResponseDTO);
  }
  // 내 리뷰 조회
  @GetMapping("/myReview")
  @PreAuthorize("hasAnyRole('OWNER', 'CUSTOMER')")
  public ResponseEntity<?> myReview(
      @RequestParam("page") int page,
      @RequestParam("size") int size,
      @RequestParam("sortBy") String sortBy,
      @RequestParam("isAsc") boolean isAsc,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    Page<ReviewListResponseDTO> reviewListResponseDTOS = reviewService.myReview(page - 1, size, sortBy, isAsc, userDetails);
    return ResponseEntity.ok().body(reviewListResponseDTOS);
  }

  // 내 리뷰 수정
  @PutMapping()
  @PreAuthorize("hasAnyRole('OWNER', 'CUSTOMER')")
  public ResponseEntity<?> myReview(
      @RequestPart("review") @Validated ReviewEditRequestDTO reviewEditRequestDTO,
      @RequestPart(value = "reviewImage", required = false) MultipartFile reviewImage,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    ReviewEditResponseDTO reviewEditResponseDTO = reviewService.reviewEdit(reviewEditRequestDTO, reviewImage, userDetails);
    return ResponseEntity.ok().body(reviewEditResponseDTO);
  }

  // 내 리뷰 삭제
  @DeleteMapping("/{reviewId}")
  @PreAuthorize("hasAnyRole('OWNER', 'CUSTOMER', 'MANAGER', 'MASTER')")
  public ResponseEntity<?> deleteReview(
      @PathVariable("reviewId") UUID reviewId,
      @AuthenticationPrincipal UserDetailsImpl userDetails
  ) {
    boolean result = reviewService.deleteReview(reviewId, userDetails);
    return ResponseEntity.ok().body(result);  // 반환값 추가
  }

}
