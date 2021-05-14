package com.SimpleShop.cart.Interceptor;


import com.SimpleShop.cart.bean.User;
import com.SimpleShop.cart.bean.UserThreadLocal;
import com.SimpleShop.cart.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UserLoginInterceptor implements HandlerInterceptor {

    @Value(value = "SSO_COOKIE")
    public String cookieName;

    @Autowired
    public com.SimpleShop.cart.service.UserService UserService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        UserThreadLocal.set(null);
        Cookie cookie = null;
        for( Cookie cook : request.getCookies() ) {

            if( cookieName.equals(cook.getName()) ) {
                cookie = cook;
            }
        }
        if( null == cookie ) {

            return true;
        }

        User user = UserService.queryUserByToken(cookie.getValue());

        UserThreadLocal.set(user);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
