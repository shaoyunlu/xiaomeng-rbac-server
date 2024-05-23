package com.xm.repository;

import com.xm.model.Menu;

import org.springframework.data.jpa.repository.JpaRepository;


public interface MenuRepository extends JpaRepository<Menu, Long> {
   
}