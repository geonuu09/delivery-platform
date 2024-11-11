package com.example.delivery.review.dto.request;

import com.example.delivery.review.entity.Review;
import com.example.delivery.user.entity.User;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class ReviewEditRequestDTO {

  @NotNull
  private UUID reviewId;

  private String content;

  @NotNull
  @Min(1)
  @Max(5)
  private int starRating;

  public Review toEntity(User user, String uploadReviewImage) {
    return Review.builder()
        .id(this.getReviewId())
        .content(this.getContent())
        .starRating(this.getStarRating())
        .reviewImage(uploadReviewImage)
        .user(user)
        .build();
  }

}
