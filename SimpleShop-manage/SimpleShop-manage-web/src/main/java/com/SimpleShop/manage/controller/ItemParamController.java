package com.SimpleShop.manage.controller;

import com.SimpleShop.commons.EasyUIResult;
import com.SimpleShop.manage.entiy.ItemParam;
import com.SimpleShop.manage.service.ItemParamService;
import com.sun.org.apache.bcel.internal.generic.RETURN;
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

/**
 * @author YGH
 *
 */
@RequestMapping("item/param")
@Controller
public class ItemParamController {

    @Autowired
    public ItemParamService itemParamService;

    Logger LOGGER = LoggerFactory.getLogger(ItemParamController.class);

    @RequestMapping( value = "{id}",method = RequestMethod.GET)
    public ResponseEntity<ItemParam> queryItemParamById(@PathVariable(value = "id") Long itemCatId) {

        LOGGER.debug("queryItemParamById param id = {}",itemCatId);

        try {

            ItemParam itemParam =  itemParamService.queryByItemCatId(itemCatId);
            LOGGER.debug("itemParamService queryById itemParam = {} ",itemParam);
            if ( null == itemParam ) {

                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);

            }

            //System.out.println("ok");
            return  ResponseEntity.status(HttpStatus.OK).body(itemParam);

        }catch (Exception e) {

            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);

        }

    }

    /**
     * @author YGH
     * 查询所有商品规格模板
     */
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<EasyUIResult> queryItemParam(
            @RequestParam(value = "rows",defaultValue = "30",required = false) Integer rows,
            @RequestParam(value = "page",defaultValue = "1",required = false) Integer page) {

        LOGGER.debug("queryItemParam param rows = {} , page = {} ",rows,page);

        try {

            EasyUIResult easyUIResult = itemParamService.queryItemParam(rows,page);
            LOGGER.debug("itemParamService queryItemParam easyUIResult = {} ",easyUIResult);

            if ( easyUIResult.getTotal()<1 ) {

                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);

            }

            return  ResponseEntity.ok().body(easyUIResult);

        }catch (Exception e) {

            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);

        }

    }

    @RequestMapping( value = "{id}",method = RequestMethod.POST)
    public ResponseEntity<Void> saveItemParam(@PathVariable(value = "id") Long itemCatId,@RequestParam("paramData") String param ) {

        LOGGER.debug("saveItemParam param itemCatId = {} , itemParam = {} ",itemCatId,param);
        ItemParam record = new ItemParam();
        record.setItemCatId(itemCatId);
        record.setParamData(param);
        if( null == record || null == itemCatId) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

        }
        record.setId(null);
        record.setItemCatId(itemCatId);

        try {

            int isSucess =  itemParamService.save(record);
            LOGGER.debug("itemParamService save isSucess = {} ",isSucess);
            if ( isSucess == 0 ) {

                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

            }
            return  ResponseEntity.status(HttpStatus.CREATED).build();

        }catch (Exception e) {

            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

        }

    }

}
