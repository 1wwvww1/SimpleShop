package com.SimpleShop.manage.controller.api;

import com.SimpleShop.manage.entiy.ItemParamItem;
import com.SimpleShop.manage.service.ItemParamItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@RequestMapping("/api/item/param/item")
@Controller
public class ApiItemParamItemController {

    @Autowired
    public ItemParamItemService itemParamItemService;

    @RequestMapping(value = "{id}",method = RequestMethod.GET)
    public ResponseEntity<ItemParamItem> queryItemParamItemById(@PathVariable(value = "id") Long itemId) {

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
