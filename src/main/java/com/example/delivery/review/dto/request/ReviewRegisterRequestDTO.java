package com.example.delivery.review.dto.request;

import com.example.delivery.review.entity.Review;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class ReviewRegisterRequestDTO {


  private String content;

  @NotNull
  @Min(1)
  @Max(5)
  private int starRating;

  public Review toEntity() {
    return Review.builder()
        .content(this.content)
        .starRating(this.starRating)
        .build();
  }
}
