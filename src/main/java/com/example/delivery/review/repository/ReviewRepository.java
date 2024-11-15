package com.example.delivery.review.repository;

import com.example.delivery.review.entity.Review;
import com.example.delivery.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ReviewRepository extends JpaRepository<Review, UUID> {


  Page<Review> findByOrder_Store_storeIdAndDeletedAtIsNull(UUID storeId, Pageable pageable);

  Page<Review> findByUserAndDeletedAtIsNull(User user, Pageable pageable);
}
