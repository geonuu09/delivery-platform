package com.example.delivery.store.repository;

import com.example.delivery.store.entity.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface StoreRepository extends JpaRepository<Store, UUID>, StoreRepositoryCustom {

    Page<Store> findByCategory_CategoryIdAndDeletedFalse(UUID categoryId, Pageable pageable);

    boolean existsByStoreNameAndDeletedFalse(String storeName);

    List<Store> findByUser_UserId(Long userId);

    boolean existsByUser_UserIdAndStoreIdAndDeletedFalse(Long userId, UUID storeId);

    Optional<Store> findByStoreIdAndDeletedFalse(UUID storeId);

}






