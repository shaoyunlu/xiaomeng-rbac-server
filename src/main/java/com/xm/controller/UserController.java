package com.xm.controller;


import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.xm.dto.MenuDto;
import com.xm.dto.UserDto;
import com.xm.mapper.UserMapper;
import com.xm.model.AjaxResponse;
import com.xm.model.Menu;
import com.xm.model.User;
import com.xm.service.MenuService;
import com.xm.service.UserService;
import com.xm.util.XmConstants;

import jakarta.servlet.http.HttpSession;


@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MenuService menuService;

    @GetMapping("/user/query")
    public AjaxResponse queryUsers(@ModelAttribute UserDto userDto){

        ModelMapper modelMapper = userMapper.getLocalModelMapper();

        Page<User> users = userService.getAllUsers(userDto);
        Page<UserDto> userDtos = users.map(user -> modelMapper.map(user ,UserDto.class));

        return AjaxResponse.ok(userDtos);
    }

    @PostMapping("/user/add")
    public AjaxResponse addUser(@RequestBody UserDto userDto) {
        
        userService.addUser(userDto);
        return AjaxResponse.ok(null);
    }

    @PostMapping("/user/delete")
    public AjaxResponse deleteUser(@RequestBody UserDto userDto) {

        userService.deleteUser(userDto);
        return AjaxResponse.ok(null);
    }

    @PostMapping("/user/batchdelete")
    public AjaxResponse batchDeleteUsers(@RequestBody List<Long> idList){

        userService.batchDeleteUsers(idList);
        return AjaxResponse.ok(null);

    }

    @PostMapping("/user/update")
    public AjaxResponse updateUser(@RequestBody UserDto userDto) {
        
        userService.updateUser(userDto);
        return AjaxResponse.ok(null);
    }

    @PostMapping("/user/password/update")
    public AjaxResponse updatePassword(HttpSession session ,@RequestBody UserDto userDto){

        AjaxResponse ajaxResponse = userService.updatePassword(userDto);

        if (ajaxResponse.getSuccess()){
            session.setAttribute(XmConstants.SESSION_USER_KEY, null);
        }

        return ajaxResponse;
    }

    @GetMapping("/user/menu/query")
    public AjaxResponse getMenusByUser(@ModelAttribute UserDto userDto ,HttpSession session){

        UserDto sessionUserDto = (UserDto)session.getAttribute(XmConstants.SESSION_USER_KEY);
        List<Menu> list = menuService.getMenusByUser(sessionUserDto);
        ModelMapper modelMapper = new ModelMapper();
        List<MenuDto> menuDtoList = list.stream()
            .map(menu -> modelMapper.map(menu, MenuDto.class))
            .collect(Collectors.toList());
        return AjaxResponse.ok(menuDtoList);
    }
}
