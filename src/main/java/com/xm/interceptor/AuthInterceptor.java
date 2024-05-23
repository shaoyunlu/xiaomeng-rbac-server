package com.xm.interceptor;

import java.util.List;
import java.util.Optional;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.xm.cache.PermissionCache;
import com.xm.dto.PermissionDto;
import com.xm.dto.UserDto;
import com.xm.util.XmConstants;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class AuthInterceptor implements HandlerInterceptor {

    PermissionCache permissionCache;

    public AuthInterceptor(PermissionCache permissionCache) {
        this.permissionCache = permissionCache;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if ("OPTIONS".equalsIgnoreCase(request.getMethod()) || "GET".equalsIgnoreCase(request.getMethod())) {
            return true; // 直接放行OPTIONS请求
        }
        UserDto user = (UserDto)request.getSession().getAttribute(XmConstants.SESSION_USER_KEY);
        if (user == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401状态码
            return false;
        }
        String requestUri = request.getRequestURI();
        // 先判断是否在权限列表内，如果在则需要验证，否则直接通过
        List<PermissionDto> allPermissions = permissionCache.getPermissionDtos();
        Optional<PermissionDto> findOne = allPermissions.stream().
                                            filter(permission->requestUri.equals(permission.getExpression())).findFirst();
        if (!findOne.isPresent()){
            return true;
        }
        
        // 需要判断是不是有执行权限
        
        if (user.getPermissionDtos() != null) {
            for (PermissionDto permission : user.getPermissionDtos()) {
                if (requestUri.equals(permission.getExpression())) {
                    return true;  // 如果找到匹配的权限，授权通过
                }
            }
        }

        response.setStatus(HttpServletResponse.SC_FORBIDDEN); // 403状态码，无权限
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        // 请求处理之后进行调用，但是在视图被渲染之前
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 在整个请求结束之后被调用，也就是在DispatcherServlet渲染了对应的视图之后执行（主要用于进行资源清理工作）
    }
}
