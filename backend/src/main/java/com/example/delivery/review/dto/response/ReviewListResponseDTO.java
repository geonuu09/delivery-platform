package com.example.delivery.review.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewListResponseDTO {
  private String content;
  private int starRating;
  private LocalDateTime createdAt;
  private String userName;
  private String reviewImage;
  private List<String> menuNameList;

//  private String storeName;




}
