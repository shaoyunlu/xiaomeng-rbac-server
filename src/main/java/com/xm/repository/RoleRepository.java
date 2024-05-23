package com.xm.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.xm.model.Role;


public interface RoleRepository extends JpaRepository<Role, Long> ,JpaSpecificationExecutor<Role> {


}