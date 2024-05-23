package com.xm.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xm.dto.PermissionDto;
import com.xm.model.Menu;
import com.xm.model.Permission;
import com.xm.model.Role;
import com.xm.repository.MenuRepository;
import com.xm.repository.PermissionRepository;

@Service
public class PermissionService {

    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Transactional(readOnly = true)
    public Page<Permission> getPermissions(PermissionDto permissionDto){

        Sort sort = Sort.by(Sort.Order.asc("menu"), Sort.Order.asc("type"));
        Pageable pageable = PageRequest.of(permissionDto.getPageNum(), permissionDto.getPageSize(),sort);

        if (permissionDto.getMenuIdsStr() == null){
            return permissionRepository.findAll(pageable);
        }
        else
        {
            String menuIdStr = permissionDto.getMenuIdsStr();
            String[] menuIdArray = menuIdStr.split(",");
            Set<Long> longSet = new HashSet<>();

            // 遍历字符串数组，将每个元素转换为Long并添加到集合中
            for (String element : menuIdArray) {
                longSet.add(Long.parseLong(element));
            }
            return permissionRepository.findByMenuIdIn(longSet, pageable);
        }
    }

    @Transactional
    public void addPermission(PermissionDto permissionDto){
        Permission permission = convertToModel(permissionDto);
        if (permissionDto.getMenuId() != null){
            Menu menu = menuRepository.findById(permissionDto.getMenuId())
                    .orElseThrow(() -> new RuntimeException("User not found with id: " + permissionDto.getMenuId()));
            permission.setMenu(menu);
        }

        permissionRepository.save(permission);
    }

    @Transactional
    public void updatePermission(PermissionDto permissionDto){
        Permission permission = permissionRepository.findById(permissionDto.getId())
                .orElseThrow(() -> new RuntimeException("User not found with id " + permissionDto.getId()));

        permission.setName(permissionDto.getName());
        permission.setType(permissionDto.getType());
        permission.setExpression(permissionDto.getExpression());
        permission.setDesc(permissionDto.getDesc());

        if (permissionDto.getMenuId() != null){
            Menu menu = menuRepository.findById(permissionDto.getMenuId())
                    .orElseThrow(() -> new RuntimeException("Menu not found with id: " + permissionDto.getMenuId()));
            permission.setMenu(menu);
        }

        permissionRepository.save(permission);
    }

    @Transactional
    public void deletePermission(PermissionDto permissionDto){
        Permission permission = permissionRepository.findById(permissionDto.getId())
                .orElseThrow(() -> new RuntimeException("permission not found with id " + permissionDto.getId()));
        for (Role role : permission.getRoles() ){
            role.getPermissions().remove(permission);
        }
        permissionRepository.deleteById(permissionDto.getId());
    }

    private Permission convertToModel(PermissionDto dto){
        Permission permission = new Permission();
        permission.setName(dto.getName());
        permission.setType(dto.getType());
        permission.setExpression(dto.getExpression());
        permission.setDesc(dto.getDesc());
        return permission;
    }
}
