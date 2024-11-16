package com.example.delivery.review.dto.response;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ReviewShowResponseDTO {

  private String content;
  private int starRating;
  private LocalDateTime createdAt;
  private String userName;
  private String reviewImage;
  private List<String> menuNameList;

//  private String storeName;
}
