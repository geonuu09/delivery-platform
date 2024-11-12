package com.example.delivery.bookmark.repository;

import com.example.delivery.bookmark.entity.Bookmark;
import com.example.delivery.store.entity.Store;
import com.example.delivery.user.entity.User;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookmarkRepository extends JpaRepository<Bookmark, UUID> {

    Optional<Bookmark> findByUserIdAndStoreId(User user, Store store);
    Page<Bookmark> findAllByUserId(Long userId, Pageable pageable);
}
