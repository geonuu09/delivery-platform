package com.example.delivery.review.entity;

import com.example.delivery.common.entity.Timestamped;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@Table(name = "p_review")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Review extends Timestamped {

  @Id
  @UuidGenerator
  @Column(name = "review_id")
  private UUID id;

  private String content;

  @Column(name = "star_rating", nullable = false)
  private int starRating;



}
