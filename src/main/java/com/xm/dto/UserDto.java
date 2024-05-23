package com.xm.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonFormat;

public class UserDto extends BaseDto{

    private Long id;

    private String name;

    private String realName;

    private String password;

    private String email;

    private Set<RoleDto> roleDtos = new HashSet<>();

    private List<PermissionDto> permissionDtos = new ArrayList<>();

    @JsonFormat(pattern = "YYYY-MM-dd")
    private LocalDateTime updateTime;

    /** ------------------------------------------- */

    private String oldPassword;

    private String checkPassword;

    private Set<Long> roleIds;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<Long> getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(Set<Long> roleIds) {
        this.roleIds = roleIds;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getCheckPassword() {
        return checkPassword;
    }

    public void setCheckPassword(String checkPassword) {
        this.checkPassword = checkPassword;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public Set<RoleDto> getRoleDtos() {
        return roleDtos;
    }

    public void setRoleDtos(Set<RoleDto> roleDtos) {
        this.roleDtos = roleDtos;
    }

    public List<PermissionDto> getPermissionDtos() {
        return permissionDtos;
    }

    public void setPermissionDtos(List<PermissionDto> permissionDtos) {
        this.permissionDtos = permissionDtos;
    }
}
