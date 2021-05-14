package com.SimpleShop.web.controller;

import com.SimpleShop.commons.EasyUIResult;
import com.SimpleShop.manage.entiy.Content;
import com.SimpleShop.web.service.IndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

import java.util.List;

@Controller
public class IndexPageController {

    @Autowired
    public IndexService indexService;

    @RequestMapping(value = "index",method = RequestMethod.GET)
    public ModelAndView toIndexPage(){

        //System.out.println("in indexToPage");
        ModelAndView modelAndView = new ModelAndView("index");
        String resultAD1 = indexService.queryIndexAD1();
        String resultAD2 = indexService.queryIndexAD2();
        String resultAD3 = indexService.queryIndexAD3();
        modelAndView.addObject("IndexAD1",resultAD1);
        modelAndView.addObject("IndexAD2",resultAD2);
        modelAndView.addObject("IndexAD3",resultAD3);

        return modelAndView;

    }

}
