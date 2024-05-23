package com.xm.repository;

import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.xm.model.Permission;

public interface PermissionRepository extends JpaRepository<Permission, Long> {
    @Query("SELECT p FROM Permission p WHERE p.menu.id IN :menuIds")
    Page<Permission> findByMenuIdIn(@Param("menuIds") Set<Long> menuIds, Pageable pageable);
}
