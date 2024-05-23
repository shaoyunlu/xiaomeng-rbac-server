package com.xm.service;


import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xm.dto.RoleDto;
import com.xm.model.AjaxResponse;
import com.xm.model.Permission;
import com.xm.model.Role;
import com.xm.repository.PermissionRepository;
import com.xm.repository.RoleRepository;
import com.xm.repository.UserRepository;
import com.xm.specification.RoleSpecification;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PermissionRepository permissionRepository;

    public List<Role> getAllRoles(){
        return roleRepository.findAll();
    }

    public Page<Role> getAllRolesWithoutAdmin(RoleDto roleDto){
        Specification<Role> spec = Specification.where(RoleSpecification.nameIsNotAdmin()).and(RoleSpecification.queryById(null));
        return roleRepository.findAll(spec ,PageRequest.of(roleDto.getPageNum() ,roleDto.getPageSize()));
    }

    public void addRole(RoleDto roleDto){
        Role role = new Role();
        role.setName(roleDto.getName());
        role.setDesc(roleDto.getDesc());
        roleRepository.save(role);
    }

    @Transactional
    public void updateRole(RoleDto roleDto){
        Role role = roleRepository.findById(roleDto.getId())
                        .orElseThrow(() -> new RuntimeException("Role not found with id: " + roleDto.getId()));
        role.setName(roleDto.getName());
        role.setDesc(roleDto.getDesc());
        roleRepository.save(role);
    }

    @Transactional
    public AjaxResponse deleteRole(RoleDto roleDto){
        // 删除角色时，需要删除用户-角色关联关系表
        Long userCount = userRepository.countByRoleId(roleDto.getId());
        if (userCount > 0){
            return AjaxResponse.fail("角色不能被删除，因为还有用户与之关联。");
        }else{
            roleRepository.deleteById(roleDto.getId());
            return AjaxResponse.ok(null);
        }
    }

    @Transactional
    public void configPermission(RoleDto roleDto){
        Role role = roleRepository.findById(roleDto.getId())
            .orElseThrow(() -> new RuntimeException("Role not found with id: " + roleDto.getId()));
        Set<Permission> permissions = new HashSet<>(permissionRepository.findAllById(roleDto.getPermissionIds()));
        role.setPermissions(permissions);
    }
}
