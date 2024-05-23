package com.xm.cache;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import com.xm.dto.PermissionDto;
import com.xm.model.Permission;
import com.xm.repository.PermissionRepository;

@Service
public class PermissionCache {
    @Autowired
    PermissionRepository permissionRepository;

    private List<PermissionDto> permissionDtos;

    @EventListener
    public void onApplicationEvent(ContextRefreshedEvent event) {
        ModelMapper modelMapper = new ModelMapper();
        List<Permission> permissions = permissionRepository.findAll();
        permissionDtos = permissions.stream().map(permission->modelMapper.map(permission ,PermissionDto.class)).collect(Collectors.toList());
    }

    public List<PermissionDto> getPermissionDtos() {
        return permissionDtos;
    }
}
