package com.SimpleShop.manage.controller;

import com.SimpleShop.manage.entiy.ItemParam;
import com.SimpleShop.manage.entiy.ItemParamItem;
import com.SimpleShop.manage.service.ItemParamItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("item/param/item")
@Controller
public class ItemParamItemController {

    @Autowired
    public ItemParamItemService itemParamItemService;

    @RequestMapping(value = "{id}")
    public ResponseEntity<ItemParamItem> queryItemParamItemById(@PathVariable(value = "id") Long itemId) {

        if( null == itemId ) {

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        try {

            ItemParamItem itemParamItem = itemParamItemService.queryByItemId(itemId);
            if( null == itemParamItem ) {

                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);

            }
            return ResponseEntity.status(HttpStatus.OK).body(itemParamItem);

        }catch (Exception e) {

            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);

        }

    }


}
