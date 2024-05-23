package com.xm.mapper;

import java.util.HashSet;
import java.util.Set;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;

import com.xm.dto.PermissionDto;
import com.xm.dto.RoleDto;
import com.xm.model.Permission;
import com.xm.model.Role;

@Service
public class RoleMapper extends BaseMapper{

    public RoleMapper(ModelMapper modelMapper){
        super(modelMapper); // 调用父类的构造函数
    }

    @Override
    protected void configureModelMapper(ModelMapper modelMapper){
        modelMapper.createTypeMap(Role.class, RoleDto.class).addMappings(mapper -> {
            mapper.<Set<PermissionDto>>map(src -> {
                // 检查permissions是否为null，如果是则提供一个空的HashSet作为源对象
                Set<Permission> permissions = src.getPermissions() == null ? new HashSet<>() : src.getPermissions();
                // 然后进行映射
                return modelMapper.map(permissions, new TypeToken<Set<PermissionDto>>() {}.getType());
            }, (dest, v) -> dest.setPermissionDtos((Set<PermissionDto>) v));
        });
    }


}
