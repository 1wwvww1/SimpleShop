package com.SimpleShop.manage.controller.api;

import com.SimpleShop.commons.EasyUIResult;
import com.SimpleShop.manage.entiy.Item;
import com.SimpleShop.manage.entiy.ItemParamItem;
import com.SimpleShop.manage.service.ItemService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequestMapping("/api/item")
@Controller
public class ApiItemController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiItemController.class);

    @RequestMapping(value = "{id}",method=RequestMethod.GET)
    public ResponseEntity<Item> queryItemById (@PathVariable("id") Long id) {

        LOGGER.debug("queryItemById requestParam: id = {}",id);

        try {

            Item item = this.itemService.queryById(id);
            LOGGER.debug("itemService item = {}",item);
            if ( null == item ) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            return ResponseEntity.ok(item);
        }catch (Exception e) {

            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }

    }

    @Autowired
    public ItemService itemService;

    @RequestMapping(method=RequestMethod.GET)
    public ResponseEntity<EasyUIResult> queryItemList(
            @RequestParam(value = "rows",defaultValue = "30",required = false) Integer rows,
            @RequestParam(value = "page",defaultValue = "1",required = false) Integer page) {

        LOGGER.debug("queryItemList requestParam: size = {} , rows = {}",page,rows);

        try {

            EasyUIResult easyUIResult = this.itemService.queryItemList(page, rows);
            LOGGER.debug("easyUIResult : total = {}",easyUIResult.getTotal());

            if ( null == easyUIResult.getRows() || easyUIResult.getTotal()<1 ) {

                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);

            }

            return ResponseEntity.ok(easyUIResult);

        }catch (Exception e) {

            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }

    }




}
