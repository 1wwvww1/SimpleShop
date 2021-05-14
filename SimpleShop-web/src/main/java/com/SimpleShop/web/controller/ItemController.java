package com.SimpleShop.web.controller;


import com.SimpleShop.manage.entiy.ItemDesc;
import com.SimpleShop.web.entiy.Item;
import com.SimpleShop.web.service.ItemService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@RequestMapping("item")
@Controller
public class ItemController {

    @Autowired
    public ItemService itemService;

    @RequestMapping(value = "{itemId}",method = RequestMethod.GET)
    public ModelAndView showItem(@PathVariable("itemId") Long itemId) {

        ModelAndView mv = new ModelAndView("item");
        Item item = itemService.queryItemById(itemId);
        if( null == item ) {
            return new ModelAndView("error/exception");
        }
        mv.addObject("item",item);

        ItemDesc itemDesc = itemService.queryItemDescByItemId(itemId);
        if( null == itemDesc ) {
            return new ModelAndView("error/exception");
        }
        mv.addObject("itemDesc",itemDesc);

        String itemParam = itemService.queryItemParamItemByItemId(itemId);
        if( StringUtils.isEmpty(itemParam) ) {
            System.out.println("null");
            return new ModelAndView("error/exception");
        }
        mv.addObject("itemParam",itemParam);

        return mv;
    }

}
