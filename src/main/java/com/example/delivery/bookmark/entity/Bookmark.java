package com.example.delivery.bookmark.entity;

import com.example.delivery.common.entity.Timestamped;
import com.example.delivery.store.entity.Store;
import com.example.delivery.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Table(
    name = "p_bookmark", uniqueConstraints = {
    @UniqueConstraint(name = "uk_bookmark_user_store",
        columnNames = {"user_id", "store_id"})
})
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Bookmark extends Timestamped {

    @Id
    @UuidGenerator(style = UuidGenerator.Style.TIME)
    private UUID bookmarkId;

    @Column(nullable = false) // null 불가 명시
    private boolean isBookmarked;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    // 북마크 상태 변경 메소드
    public void toggleBookmark() {
        this.isBookmarked = !this.isBookmarked;
    }

    // 팩토리 메소드
    public static Bookmark create(User user, Store store) {
        return Bookmark.builder()
            .user(user)
            .store(store)
            .isBookmarked(true)
            .build();
    }
}