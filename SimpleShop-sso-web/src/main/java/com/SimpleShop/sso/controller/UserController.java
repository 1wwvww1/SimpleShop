package com.SimpleShop.sso.controller;

import com.SimpleShop.sso.entiy.User;
import com.SimpleShop.sso.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("user")
@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "register",method = RequestMethod.GET)
    public String toRegister() {
        return "register";
    }


    @RequestMapping(value = "{param}/{type}",method = RequestMethod.GET)
    public ResponseEntity<Boolean> checkRegister(@PathVariable("param") String param, @PathVariable("type") Integer type) {

        try {
            if (!StringUtils.isEmpty(param) && null != type) {
                Boolean res = userService.checkRegister(param, type);

                //为前端页面显示有问题，需取反
                if (res == null) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(false);
                }

                if( res ) {

                    return ResponseEntity.status(HttpStatus.OK).body(false);
                }

                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(true);
            }

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(false);
        } catch ( Exception e ) {

            e.printStackTrace();
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    @RequestMapping(value = "doRegister",method = RequestMethod.POST)
    public ResponseEntity<Map<String,Object>> doRegister(@Valid User user , BindingResult BindingResult){

        Map<String,Object> result = new HashMap<>();
        try {

            if ( BindingResult.hasErrors() ) {

                List<String> msg = new ArrayList<>();
                List<ObjectError> errors = BindingResult.getAllErrors();
                for ( ObjectError objectError : errors ) {

                    msg.add(objectError.getDefaultMessage());
                }

                result.put("status",400);
                result.put("data",StringUtils.join(msg,'|'));
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
            }

            Boolean sucess = userService.register(user);
            if(!sucess) {

                result.put("status",400);
                result.put("data","注册失败");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
            }

            result.put("status",200);
            return ResponseEntity.status(HttpStatus.OK).body(result);

        } catch ( Exception e ) {

            e.printStackTrace();
        }

        result.put("status",500);
        result.put("data","注册失败,请联系客服或网站管理员");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
    }

    @RequestMapping(value = "login",method = RequestMethod.GET)
    public String toLogin() {
        return "login";
    }

    @RequestMapping(value = "doLogin",method = RequestMethod.POST)
    public ResponseEntity<Map<String,Object>> doLogin(User user , HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){

        Map<String,Object> result = new HashMap<>();
        if( StringUtils.isEmpty(user.getUsername()) || StringUtils.isEmpty(user.getPassword()) ) {

            result.put("status",404);
            result.put("data","登录失败,用户名或密码错误");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
        }
        try {

            String SIMPLE_TOKEN = userService.login(user);
            if(StringUtils.isEmpty(SIMPLE_TOKEN)) {

                result.put("status",404);
                result.put("data","登录失败,用户名或密码错误");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
            }
            Cookie cookie = new Cookie("SIMPLE_TOKEN",SIMPLE_TOKEN);
            cookie.setMaxAge(60*60);
            String domain = httpServletRequest.getRequestURL().toString();
            domain = StringUtils.split(domain,".")[1];
            cookie.setDomain(domain+".com");
            cookie.setPath("/");
            httpServletResponse.addCookie(cookie);

            result.put("status",200);
            return ResponseEntity.status(HttpStatus.OK).body(result);

        } catch ( Exception e ) {

            e.printStackTrace();
        }

        result.put("status",500);
        result.put("data","登录失败,请联系客服或网站管理员");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
    }

    @RequestMapping(value = "{tokens}",method = RequestMethod.GET)
    public ResponseEntity<User> queryByToken(@PathVariable String tokens) {

        try {

            User user = userService.queryByToken(tokens);
            if ( null == user ) {

                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            return ResponseEntity.status(HttpStatus.OK).body(user);
        } catch ( Exception e ) {

            e.printStackTrace();
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

}
