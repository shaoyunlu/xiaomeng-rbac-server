package com.xm.controller;


import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.xm.dto.RoleDto;
import com.xm.mapper.RoleMapper;
import com.xm.model.AjaxResponse;
import com.xm.model.Role;
import com.xm.service.RoleService;

@RestController
public class RoleController {

    @Autowired
    private RoleService roleService;

    @Autowired
    private RoleMapper roleMapper;

    @GetMapping("/role/query")
    public AjaxResponse queryAllRoles(){
        List<Role> list = roleService.getAllRoles();
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.addMappings(new PropertyMap<Role, RoleDto>() {
            @Override
            protected void configure() {
                skip(destination.getPermissionDtos());
            }
        });
        List<RoleDto> dtoList = list.stream().map(role -> modelMapper.map(role, RoleDto.class))
                                    .collect(Collectors.toList());
        return AjaxResponse.ok(dtoList);
    }

    @GetMapping("/role/filterquery")
    public AjaxResponse getAllRolesWithoutAdmin(@ModelAttribute RoleDto roleDto){
        ModelMapper modelMapper = roleMapper.getLocalModelMapper();
        Page<Role> pageInfo = roleService.getAllRolesWithoutAdmin(roleDto);
        Page<RoleDto> pageDtoInfo = pageInfo.map(role -> modelMapper.map(role ,RoleDto.class));
        return AjaxResponse.ok(pageDtoInfo);
    }

    @PostMapping("/role/add")
    public AjaxResponse addRole(@RequestBody RoleDto roleDto){
        roleService.addRole(roleDto);
        return AjaxResponse.ok(null);
    }

    @PostMapping("/role/update")
    public AjaxResponse updateRole(@RequestBody RoleDto roleDto){

        roleService.updateRole(roleDto);
        return AjaxResponse.ok(null);
    }

    @PostMapping("/role/delete")
    public AjaxResponse deleteRole(@RequestBody RoleDto roleDto){

        return roleService.deleteRole(roleDto);
    }

    @PostMapping("/role/permission/config")
    public AjaxResponse configPermission(@RequestBody RoleDto roleDto){

        roleService.configPermission(roleDto);
        return AjaxResponse.ok(null);
    }
}
