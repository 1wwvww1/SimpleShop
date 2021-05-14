package com.SimpleShop.sso.controller;

import com.SimpleShop.sso.entiy.User;
import com.SimpleShop.sso.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;

@Controller
@RequestMapping("api/user")
public class ApiUserController {

    @Autowired
    public UserService userService;

    public ResponseEntity<User> queryUser(@RequestParam("token") String token) {

        try {

            User user = userService.queryByToken(token);
            return ResponseEntity.status(HttpStatus.OK).body(user);

        } catch ( Exception e ) {

            e.printStackTrace();
        }

        return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
}
