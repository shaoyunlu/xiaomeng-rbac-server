package com.xm.controller;

import java.util.Map;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.xm.dto.UserDto;
import com.xm.model.AjaxResponse;
import com.xm.service.UserService;
import com.xm.util.XmConstants;

import jakarta.servlet.http.HttpSession;

@RestController
public class LoginController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public AjaxResponse login(@RequestBody Map<String, Object> jsonPayload ,HttpSession session) {
        String sessionCaptcha = (String)session.getAttribute(XmConstants.CAPTCHA_KEY);
        if (!sessionCaptcha.equals(jsonPayload.get("validateNum")) ){
            return AjaxResponse.fail("验证码不正确");
        }
        UserDto userDto = userService.login((String)jsonPayload.get("name"));

        if (userDto == null){
            return AjaxResponse.fail("用户不存在");
        }

        if (!BCrypt.checkpw((String)jsonPayload.get("password"), userDto.getPassword())){
            return AjaxResponse.fail("密码不正确");
        }

        userDto.setPassword(null);

        session.setAttribute(XmConstants.SESSION_USER_KEY, userDto);
        return AjaxResponse.ok(userDto);
    }

    @PostMapping("/logout")
    public AjaxResponse logout(HttpSession session){

        session.setAttribute(XmConstants.SESSION_USER_KEY, null);

        return AjaxResponse.ok(null);
    }
}
