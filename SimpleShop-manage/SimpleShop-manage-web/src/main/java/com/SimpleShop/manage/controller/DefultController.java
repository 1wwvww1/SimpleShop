package com.SimpleShop.manage.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@RequestMapping("page")
@Controller
public class DefultController {

    @RequestMapping(value = "{pageName}", method = RequestMethod.GET)
    public String toPage(@PathVariable("pageName") String pageName)  {

        //System.out.println("topage-----"+pageName);

        return pageName;

    }

}
