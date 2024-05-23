package com.xm.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xm.dto.MenuDto;
import com.xm.model.AjaxResponse;
import com.xm.model.Menu;
import com.xm.service.MenuService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
public class MenuController {
    @Autowired
    private MenuService menuService;

    @GetMapping("/menu/query")
    public AjaxResponse queryAllMenus(){
        List<Menu> list = menuService.getAllMenus();
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.addMappings(new PropertyMap<Menu, MenuDto>() {
            @Override
            protected void configure() {
                skip(destination.getPermissionDtos());
            }
        });
        List<MenuDto> dtoList = list.stream()
                            .map(menu -> modelMapper.map(menu, MenuDto.class))
                            .collect(Collectors.toList());
        return AjaxResponse.ok(dtoList);
    }

    @PostMapping("/menu/add")
    public AjaxResponse addMenu(@RequestBody MenuDto menuDto) {
        menuService.addMenu(menuDto);
        
        return AjaxResponse.ok(null);
    }

    @PostMapping("/menu/delete")
    public AjaxResponse deleteMenu(@RequestBody MenuDto menuDto){
        menuService.deleteMenu(menuDto);

        return AjaxResponse.ok(null);
    }

    @PostMapping("/menu/update")
    public AjaxResponse updateMenu(@RequestBody MenuDto menuDto){
        menuService.updateMenu(menuDto);

        return AjaxResponse.ok(null);
    }

    @PostMapping("/menu/order/adjust")
    public AjaxResponse adjustOrder(@RequestBody MenuDto menuDto){

        menuService.adjustOrder(menuDto);
        return AjaxResponse.ok(null);
    }
}
