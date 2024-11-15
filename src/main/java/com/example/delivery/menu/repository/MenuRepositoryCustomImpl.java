package com.example.delivery.menu.repository;

import com.example.delivery.menu.entity.Menu;
import com.example.delivery.menu.entity.QMenu;
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
public class MenuRepositoryCustomImpl implements MenuRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<Menu> findMenusByStore_StoreIdAndKeyword(UUID storeId, String keyword, Pageable pageable) {
        QMenu menu = QMenu.menu;

        List<Menu> menus = jpaQueryFactory
                .selectFrom(menu).distinct()
                .leftJoin(menu.menuOptions).fetchJoin()
                .where(
                        menu.store.storeId.eq(storeId)
                                .and(
                                        menu.hidden.eq(false)
                                                .and(menu.deleted.eq(false))
                                                .and(
                                                        menu.menuName.contains(keyword)
                                                        .or(menu.menuDescription.contains(keyword))
                                                        .or(menu.menuOptions.any().optionName.contains(keyword))

                                                )

                                )

                )
                .fetch();

        long total = menus.size();

        return new PageImpl<>(menus, pageable, total);
    }

}
