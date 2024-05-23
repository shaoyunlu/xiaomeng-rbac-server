package com.xm.specification;

import org.springframework.data.jpa.domain.Specification;

import com.xm.model.Role;
import com.xm.util.XmConstants;

public class RoleSpecification {

    public static Specification<Role> nameIsNotAdmin() {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.notEqual(root.get("name"), XmConstants.ADMIN_ROLE_NAME_KEY);
    }

    public static Specification<Role> queryById(Long roleId){
        return (root,query,criteriaBuilder) -> {
            if (roleId == null) {
                return criteriaBuilder.isTrue(criteriaBuilder.literal(true)); // 如果搜索文本为空，则不应用过滤
            }
            return criteriaBuilder.equal(root.get("id"), "roleId");
        };

    }
}
