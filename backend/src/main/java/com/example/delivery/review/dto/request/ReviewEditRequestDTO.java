package com.example.delivery.review.dto.request;

import com.example.delivery.review.entity.Review;
import com.example.delivery.user.entity.User;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class ReviewEditRequestDTO {


  private String content;

  @NotNull
  @Min(1)
  @Max(5)
  private int starRating;

  public Review toEntity(User user, String uploadReviewImage, Review review) {
    return Review.builder()
        .id(review.getId())
        .content(this.getContent())
        .starRating(this.getStarRating())
        .reviewImage(uploadReviewImage)
        .user(user)
        .order(review.getOrder())
        .build();
  }

}
