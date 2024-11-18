package com.example.delivery.store.repository;

import com.example.delivery.store.entity.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface StoreRepositoryCustom {
    Page<Store> findStoresByCategoryAndKeyword(UUID categoryId, String keyword, Pageable pageable);

    Page<Store> findStoresByKeyword(String keyword, Pageable pageable);

}
