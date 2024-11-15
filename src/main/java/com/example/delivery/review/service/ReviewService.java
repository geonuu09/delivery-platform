package com.example.delivery.review.service;

import com.example.delivery.auth.security.UserDetailsImpl;
import com.example.delivery.cart.entity.Cart;
import com.example.delivery.common.Util.PagingUtil;
import com.example.delivery.common.exception.CustomException;
import com.example.delivery.common.exception.code.ErrorCode;
import com.example.delivery.order.entity.Order;
import com.example.delivery.order.repository.OrderRepository;
import com.example.delivery.review.dto.request.ReviewEditRequestDTO;
import com.example.delivery.review.dto.response.ReviewEditResponseDTO;
import com.example.delivery.review.dto.response.ReviewListResponseDTO;
import com.example.delivery.review.dto.request.ReviewRegisterRequestDTO;
import com.example.delivery.review.dto.response.ReviewShowResponseDTO;
import com.example.delivery.review.entity.Review;
import com.example.delivery.review.repository.ReviewRepository;
import com.example.delivery.store.repository.StoreRepository;
import com.example.delivery.user.entity.User;
import com.example.delivery.user.entity.UserRoleEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewService {
  private final ReviewRepository reviewRepository;
  private final StoreRepository storeRepository;
  private final OrderRepository orderRepository;


  // 리뷰 등록
  public boolean reviewRegister(ReviewRegisterRequestDTO reviewRegisterRequestDTO, UserDetailsImpl userDetails, MultipartFile reviewImage) {
    UUID orderId = reviewRegisterRequestDTO.getOrderId();
    Order order = orderRepository.findById(orderId)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ORDER));
    // 주문이 배달 완료 상태일때 리뷰 등록 가능
    if (order.getOrderStatus().equals(Order.OrderStatus.DELIVERED)) {
      // 파일명 변경
      String reviewImagePath = uploadReviewImage(reviewImage);
      Review entity = reviewRegisterRequestDTO.toEntity(userDetails.getUser(), order, reviewImagePath);
      reviewRepository.save(entity);
      return true;
    } else {
      return false;
    }

  }


  // 리뷰 전체 조회
  @Transactional
  public Page<ReviewShowResponseDTO> reviewShow(UUID storeId, int page, int size, String sortBy, boolean isAsc, UserDetailsImpl userDetails) {
    Pageable pageable = PagingUtil.createPageable(page, size, isAsc, sortBy);
    // 삭제된 리뷰 제외

    Page<Review> reviewList = reviewRepository.findByOrder_Store_storeIdAndDeletedAtIsNull(storeId, pageable);
    User user = userDetails.getUser();
    Map<UUID, List<String>> menuNameMap = new HashMap<>();
    for (Review review : reviewList) {
      menuNameMap.put(review.getId(), new ArrayList<>());
      List<String> menuNameList = menuNameMap.get(review.getId());
      for (Cart cart : review.getOrder().getCarts()) {
        menuNameList.add(cart.getMenu().getMenuName());
      }
    }

      return reviewList.map(review ->
          ReviewShowResponseDTO.builder()
              .content(review.getContent())
              .starRating(review.getStarRating())
              .createdAt(review.getCreatedAt())
              .reviewImage(review.getReviewImage())
              .userName(user.getUserName())
              .menuNameList(menuNameMap.get(review.getId()))
              .build());

  }

  // 내 리뷰 정보 보기
  @Transactional
  public Page<ReviewListResponseDTO> myReview(int page, int size, String sortBy, boolean isAsc, UserDetailsImpl userDetails) {
    Pageable pageable = PagingUtil.createPageable(page, size, isAsc, sortBy);
    // 삭제된 리뷰 제외
    Page<Review> reviewList = reviewRepository.findByUserAndDeletedAtIsNull(userDetails.getUser(), pageable);
    User user = userDetails.getUser();
    Map<UUID, List<String>> menuNameMap = new HashMap<>();
    for (Review review : reviewList) {
      menuNameMap.put(review.getId(), new ArrayList<>());
      List<String> menuNameList = menuNameMap.get(review.getId());
      for (Cart cart : review.getOrder().getCarts()) {
        menuNameList.add(cart.getMenu().getMenuName());
      }
    }
    return reviewList.map(review ->
        ReviewListResponseDTO.builder()
            .content(review.getContent())
            .starRating(review.getStarRating())
            .createdAt(review.getCreatedAt())
            .reviewImage(review.getReviewImage())
            .userName(user.getUserName())
            .menuNameList(menuNameMap.get(review.getId()))
            .build()
    );
  }

  // 내 리뷰 수정
  public ReviewEditResponseDTO reviewEdit(ReviewEditRequestDTO reviewEditRequestDTO, MultipartFile reviewImage, UserDetailsImpl userDetails, UUID reviewId) {
    Review review = reviewRepository.findById(reviewId)
        .orElseThrow(() -> new CustomException(ErrorCode.REVIEW_NOT_FOUND));

    if (review.getDeletedAt() != null) {
      throw new CustomException(ErrorCode.REVIEW_DELETE_ALREADY);
    }

    if (review.getUser().getUserId().equals(userDetails.getUser().getUserId())) {
      User user = userDetails.getUser();
      String uploadReviewImage = uploadReviewImage(reviewImage);

      review = reviewRepository.save(reviewEditRequestDTO.toEntity(user, uploadReviewImage, review));
      return new ReviewEditResponseDTO(review);
    }
    throw new CustomException(ErrorCode.REVIEW_NOT_MATCH_USER);
  }

  // 파일명 변경 (사진 이름 중복 방지)
  private String uploadReviewImage(MultipartFile reviewImage) {
    String reviewImagePath = null;
    if (reviewImage != null) {
      reviewImagePath = UUID.randomUUID() + "_" + reviewImage.getOriginalFilename();
    }
    return reviewImagePath;
  }

  // 리뷰 삭제
  public boolean deleteReview(UUID reviewId, UserDetailsImpl userDetails) {
    Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new CustomException(ErrorCode.REVIEW_NOT_FOUND));
    if (review.getDeletedAt() != null) {
      throw new CustomException(ErrorCode.REVIEW_DELETE_ALREADY);
    }
    if (userDetails.getUser().getRole().equals(UserRoleEnum.CUSTOMER) || userDetails.getUser().getRole().equals(UserRoleEnum.OWNER)) {
      if (review.getUser().getUserId().equals(userDetails.getUser().getUserId())) {
        review.markAsDeleted(userDetails.getUsername());
        reviewRepository.save(review);
        return true;
      }
    } else {  // 관리자 삭제
      review.markAsDeleted(userDetails.getUsername());
      reviewRepository.save(review);
      return true;
    }

    throw new CustomException(ErrorCode.REVIEW_NOT_MATCH_USER);
  }
}
