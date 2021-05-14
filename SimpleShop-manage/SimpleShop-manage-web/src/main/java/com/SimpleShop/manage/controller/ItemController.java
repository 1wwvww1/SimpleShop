package com.SimpleShop.manage.controller;

import com.SimpleShop.commons.EasyUIResult;
import com.SimpleShop.manage.entiy.Item;
import com.SimpleShop.manage.entiy.ItemParam;
import com.SimpleShop.manage.entiy.ItemParamItem;
import com.SimpleShop.manage.service.ItemService;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.protocol.HTTP;
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

@RequestMapping("item")
@Controller
public class ItemController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ItemController.class);

    @Autowired
    public ItemService itemService;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Void> addItem (Item item, ItemParamItem itemParamItem, @RequestParam("desc") String desc ){

        //System.out.println("item:"+item+"--itemParam:"+itemParamItem+"--desc:"+desc);
        LOGGER.info("ItemController addItem Param : item = {} , desc = {}",item,desc);

        if ( item == null || StringUtils.isEmpty(desc) || null == itemParamItem ) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        try {
            itemService.addItem(item,itemParamItem,desc);
            System.out.println("sucess");
        }catch (Exception e) {
            e.printStackTrace();
            LOGGER.info("ItemController addItem Param : item = {} , desc = {}",item,desc);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        return ResponseEntity.status(HttpStatus.CREATED).build();

    }

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


    @RequestMapping(method=RequestMethod.PUT)
    public ResponseEntity<Void> updateItem(Item item,ItemParamItem itemParamItem,
            @RequestParam(value = "desc") String desc) {

        LOGGER.debug("updateItem requestParam: iteim = {} ,itemParamItem={} , desc = {}",item,itemParamItem,desc);

        System.out.println(item+"--"+itemParamItem+"--"+desc);

        try {

            boolean isSucess = itemService.updateItem(item,itemParamItem,desc);

            LOGGER.debug("itemService.updateItem : isSucess = {}" ,isSucess);

            if(!isSucess) {
                return  ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

        }catch (Exception e) {

            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

        }

    }

    @RequestMapping(method=RequestMethod.DELETE)
    public ResponseEntity<Void> deleteItemById(@RequestParam(value = "ids") List<Object> ids ) {

        //ids.forEach(id->System.out.println(id));
        LOGGER.debug("updateItem requestParam: ids = {}",ids);

        try {

            boolean isSucess = itemService.deleteItemById(ids);

            LOGGER.debug("itemService.deleteItemById : isSucess = {}" ,isSucess);

            if(!isSucess) {
                return  ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            return ResponseEntity.status(HttpStatus.OK).build();

        }catch (Exception e) {

            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

        }

    }


}
