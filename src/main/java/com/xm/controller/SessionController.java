package com.xm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xm.dto.UserDto;
import com.xm.mapper.UserMapper;
import com.xm.model.AjaxResponse;
import com.xm.util.XmConstants;

import jakarta.servlet.http.HttpSession;

@RestController
public class SessionController {

    @Autowired
    UserMapper userMapper;
    
    @GetMapping("/session/user")
    public AjaxResponse getSessionUser(HttpSession session){

        UserDto userDto = (UserDto)session.getAttribute(XmConstants.SESSION_USER_KEY); 
        return AjaxResponse.ok(userDto);
    }
}
