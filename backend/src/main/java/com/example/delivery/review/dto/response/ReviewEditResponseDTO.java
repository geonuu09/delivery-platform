package com.example.delivery.review.dto.response;

import com.example.delivery.review.entity.Review;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewEditResponseDTO {
  private String content;
  private int starRating;
  private LocalDateTime updatedAt;
  private String userName;
  private String reviewImage;

  public ReviewEditResponseDTO(Review review) {
    this.content = review.getContent();
    this.starRating = review.getStarRating();
    this.updatedAt = review.getUpdatedAt();
    this.userName = review.getUser().getUserName();
    this.reviewImage = review.getReviewImage();
  }
}
