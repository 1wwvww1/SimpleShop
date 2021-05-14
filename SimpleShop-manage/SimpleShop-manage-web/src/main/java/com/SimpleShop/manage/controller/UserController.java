package com.SimpleShop.manage.controller;

import com.SimpleShop.manage.entiy.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("user")
@Controller
public class UserController {

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<User> isExist(
            @RequestParam("userName") String name,
            @RequestParam("passWord") String pwd
    ) {

        User user = new User();

        if ("admin".equals(name) && "admin".equals(pwd)) {

            user.setName(name);

            user.setPassword(pwd);
            //System.out.println(user);

            return ResponseEntity.ok(null);

        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);

    }



}
