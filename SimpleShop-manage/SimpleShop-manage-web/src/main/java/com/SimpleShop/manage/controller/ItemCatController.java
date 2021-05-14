package com.SimpleShop.manage.controller;

import com.SimpleShop.manage.entiy.ItemCat;
import com.SimpleShop.manage.service.ItemCatService;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequestMapping("item/cat")
@Controller
public class ItemCatController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ItemCatController.class);

   @Autowired
   public ItemCatService itemCatService;

    @RequestMapping( method = RequestMethod.GET )
    public ResponseEntity<List<ItemCat>> itemCatListByParentId( @RequestParam(value = "id",defaultValue = "0",required = false) Long ID ) {

       // System.out.println("itemCatListByParentId");

        LOGGER.debug("ItemCatController ListItem Param : ItemCatID = {}",ID);

        List<ItemCat> itemCats ;

        itemCats = itemCatService.queryItemCatByParentId( ID );

        LOGGER.debug("ItemCatController ListItem : ItemCatID = {} , ItemcatName = {} ",itemCats.get(0).getId(),itemCats.get(0).getName());

        //System.out.println(itemCats);

        if ( itemCats == null ) {

             return ResponseEntity.status(HttpStatus.SC_NOT_FOUND).body(null);

        }

        return  ResponseEntity.ok( itemCats );

    }

}
