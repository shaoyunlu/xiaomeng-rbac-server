package com.xm.specification;

import org.springframework.data.jpa.domain.Specification;

import com.xm.model.User;

import jakarta.persistence.criteria.Predicate;

public class UserSpecification {
    public static Specification<User> nameOrRealNameContains(String keyword) {
        return (root, query, criteriaBuilder) -> {
            if (keyword == null || keyword.isEmpty()) {
                return criteriaBuilder.isTrue(criteriaBuilder.literal(true)); // 如果搜索文本为空，则不应用过滤
            }
            Predicate namePredicate = criteriaBuilder.like(root.get("name"), "%" + keyword + "%");
            Predicate nickNamePredicate = criteriaBuilder.like(root.get("realName"), "%" + keyword + "%");
            return criteriaBuilder.or(namePredicate, nickNamePredicate);
        };
    }

    public static Specification<User> nameIsNotAdmin() {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.notEqual(root.get("name"), "admin");
    }
}
