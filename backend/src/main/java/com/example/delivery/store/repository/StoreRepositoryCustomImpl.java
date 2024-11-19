package com.example.delivery.store.repository;

import com.example.delivery.store.entity.QStore;
import com.example.delivery.store.entity.Store;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class StoreRepositoryCustomImpl implements StoreRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<Store> findStoresByCategoryAndKeyword(UUID categoryId, String keyword, Pageable pageable) {

        QStore store = QStore.store;

        List<Store> stores = jpaQueryFactory
                .selectFrom(store).distinct()
                .leftJoin(store.category).fetchJoin()
                .leftJoin(store.menus).fetchJoin()
                .where(
                        store.deleted.eq(false)
                                .and(store.category.categoryId.eq(categoryId))
                                .and(
                                        store.storeName.containsIgnoreCase(keyword)
                                        .or(store.menus.any().menuDescription.containsIgnoreCase(keyword))
                                        .or(store.menus.any().menuName.containsIgnoreCase(keyword))
                                )
                )
                .fetch();

        long total = stores.size();

        return new PageImpl<>(stores, pageable, total);
    }

    @Override
    public Page<Store> findStoresByKeyword(String keyword, Pageable pageable) {

        QStore store = QStore.store;

        List<Store> stores = jpaQueryFactory
                .selectFrom(store).distinct()
                .leftJoin(store.category).fetchJoin()
                .leftJoin(store.menus).fetchJoin()
                .where(
                        store.deleted.eq(false)
                                .and(
                                        store.storeName.containsIgnoreCase(keyword)
                                        .or(store.category.categoryName.containsIgnoreCase(keyword))
                                        .or(store.menus.any().menuDescription.containsIgnoreCase(keyword))
                                        .or(store.menus.any().menuName.containsIgnoreCase(keyword))
                                )
                )
                .fetch();

        long total = stores.size();

        return new PageImpl<>(stores, pageable, total);
    }
}
