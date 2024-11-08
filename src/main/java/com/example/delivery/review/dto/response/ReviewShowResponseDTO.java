package com.example.delivery.review.dto.response;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ReviewShowResponseDTO {

  private String content;
  private int starRating;
}
