package com.example.delivery.review.entity;

import com.example.delivery.common.entity.Timestamped;
import com.example.delivery.store.entity.Store;
import com.example.delivery.user.entity.User;
import jakarta.persistence.*;
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

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "store_id")
  private Store store;






}
