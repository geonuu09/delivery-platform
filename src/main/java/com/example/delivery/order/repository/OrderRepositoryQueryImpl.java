package com.example.delivery.order.repository;

import com.example.delivery.order.entity.Order;
import com.example.delivery.order.entity.QOrder;
import com.example.delivery.user.entity.UserRoleEnum;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryQueryImpl implements OrderRepositoryQuery {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<Order> searchOrderListByKeyword(Long userId, UserRoleEnum userRole, String keyword, Pageable pageable) {
        QOrder order = QOrder.order;

        BooleanBuilder whereClause = new BooleanBuilder();

        // 가게 이름 또는 메뉴 이름에 키워드 포함
        whereClause.and(
                order.store.storeName.containsIgnoreCase(keyword)
                        .or(order.store.menus.any().menuName.containsIgnoreCase(keyword))
        );

        // 권한별 조건 추가
        if (userRole == UserRoleEnum.CUSTOMER) {
            whereClause.and(order.user.userId.eq(userId)); // 고객 : 자신의 주문

        } else if (userRole == UserRoleEnum.OWNER) {
            whereClause.and(order.store.user.userId.eq(userId)); // 점주 : 자신의 가게 주문

        } else if (userRole == UserRoleEnum.MANAGER || userRole == UserRoleEnum.MASTER) {
            whereClause.and(
                    order.user.email.containsIgnoreCase(keyword) // 관리자 : 고객 이메일, 점주 이름에서도 검색
                            .or(order.store.user.userName.containsIgnoreCase(keyword))
            );
        }

        // QueryDSL 쿼리 실행
        List<Order> orders = jpaQueryFactory
                .selectFrom(order)
                .leftJoin(order.store).fetchJoin()
                .leftJoin(order.user).fetchJoin()
                .where(whereClause)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = orders.size();

        return new PageImpl<>(orders, pageable, total);
    }
}

