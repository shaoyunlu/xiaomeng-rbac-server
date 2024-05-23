package com.xm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.xm.model.User;


public interface UserRepository extends JpaRepository<User, Long> ,JpaSpecificationExecutor<User> {

    User findByName(String name);

    @Query("SELECT COUNT(u) FROM User u JOIN u.roles r WHERE r.id = :roleId")
    Long countByRoleId(@Param("roleId") Long roleId);

}