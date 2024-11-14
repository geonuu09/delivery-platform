package com.example.delivery.category.entity;

import com.example.delivery.common.entity.Timestamped;
import com.example.delivery.category.dto.CategoryRequestDto;
import com.example.delivery.store.entity.Store;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Table(name = "p_category")
public class Category extends Timestamped {

    @Id
    @UuidGenerator
    private UUID categoryId;

    @Column(nullable = false, unique = true)
    private String categoryName;

    private boolean deleted;

    @OneToMany(mappedBy = "category")
    private List<Store> stores = new ArrayList<>();


    public void update(CategoryRequestDto categoryRequestDto) {
        this.categoryName = categoryRequestDto.getCategoryName() != null ? categoryRequestDto.getCategoryName() : this.categoryName;
    }

    public void delete(String username) {
        this.deleted = true;
        this.setDeletedAt(LocalDateTime.now());
        this.setDeletedBy(username);
    }

}
