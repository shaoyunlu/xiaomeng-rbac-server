package com.xm.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.xm.dto.PermissionDto;
import com.xm.model.AjaxResponse;
import com.xm.model.Permission;
import com.xm.service.PermissionService;

@RestController
public class PermissionController {

    @Autowired
    private PermissionService permissionService;
    
    @GetMapping("/permission/query")
    public AjaxResponse queryPermissions(@ModelAttribute PermissionDto permissionDto){

        Page<Permission> modelList = permissionService.getPermissions(permissionDto);

        ModelMapper modelMapper = new ModelMapper();
        Page<PermissionDto> dtoList = modelList.map(permission -> modelMapper.map(permission, PermissionDto.class));

        return AjaxResponse.ok(dtoList);
    }

    @PostMapping("/permission/add")
    public AjaxResponse addPermission(@RequestBody PermissionDto permissionDto){

        permissionService.addPermission(permissionDto);
        return AjaxResponse.ok(null);
    }

    @PostMapping("/permission/update")
    public AjaxResponse updatePermission(@RequestBody PermissionDto permissionDto){

        permissionService.updatePermission(permissionDto);
        return AjaxResponse.ok(null);
    }

    @PostMapping("/permission/delete")
    public AjaxResponse deletePermission(@RequestBody PermissionDto permissionDto){

        permissionService.deletePermission(permissionDto);
        return AjaxResponse.ok(null);
    }
}
