package com.xm.controller;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.xm.util.XmConstants;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;

import javax.imageio.ImageIO;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@RestController
public class CaptchaController {

    @Autowired
    private DefaultKaptcha defaultKaptcha;

    @GetMapping("/captcha")
    public ResponseEntity<byte[]> getCaptcha(HttpServletRequest request) throws Exception {
        // 生成验证码文字
        //String text = defaultKaptcha.createText();
        String text = "9999";
        request.getSession().setAttribute(XmConstants.CAPTCHA_KEY, text);

        // 生成验证码图片
        BufferedImage image = defaultKaptcha.createImage(text);

        // 将图片转换为字节数组
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", baos);

        // 设置响应头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);

        return new ResponseEntity<>(baos.toByteArray(), headers, HttpStatus.OK);
    }
}

