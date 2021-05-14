package com.SimpleShop.manage.controller.api;

import com.SimpleShop.manage.entiy.ItemDesc;
import com.SimpleShop.manage.service.ItemDescService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author YGH
 *
 */
@RequestMapping("api/item/desc")
@Controller
public class ApiItemDescController {

    @Autowired
    public ItemDescService itemDescService;

    Logger LOGGER = LoggerFactory.getLogger(ApiItemDescController.class);

    @RequestMapping(value ="{itemId}",method = RequestMethod.GET)
    public ResponseEntity<ItemDesc> queryItemDescByItemId(@PathVariable("itemId") Long itemId) {

        LOGGER.debug("queryItemDesc param itemId = {} ",itemId);

        try {

            ItemDesc itemDesc = itemDescService.queryById(itemId);

            LOGGER.debug("queryItemDesc itemDescService itemDesc = {} ",itemDesc);

            if( itemDesc == null  || itemDesc.getItemId() == null ) {

                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);

            }

            return  ResponseEntity.status(HttpStatus.OK).body(itemDesc);

        }catch (Exception e) {

            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);

        }

    }

}
