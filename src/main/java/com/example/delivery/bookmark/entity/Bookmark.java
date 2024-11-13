package com.example.delivery.bookmark.entity;

import com.example.delivery.common.entity.Timestamped;
import com.example.delivery.store.entity.Store;
import com.example.delivery.user.entity.User;
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
    name = "p_bookmark",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_bookmark_user_store",
            columnNames = {"user_id", "store_id"}
        )
    }
)
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Bookmark extends Timestamped {

    @Id
    @UuidGenerator(style = UuidGenerator.Style.TIME)
    private UUID bookmarkId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    // 팩토리 메소드
    public static Bookmark create(User user, Store store) {
        return Bookmark.builder()
            .user(user)
            .store(store)
            .build();
    }
}