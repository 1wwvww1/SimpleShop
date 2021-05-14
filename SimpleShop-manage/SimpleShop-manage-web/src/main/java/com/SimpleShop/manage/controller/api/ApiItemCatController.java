package com.SimpleShop.manage.controller.api;

import com.SimpleShop.manage.entiy.ItemCatResult;
import com.SimpleShop.manage.service.ItemCatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@RequestMapping("api/item/cat")
@Controller
public class ApiItemCatController {

    @Autowired
    public ItemCatService itemCatService;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<ItemCatResult> queryItemCat() {

        try {

            ItemCatResult itemCatResult = itemCatService.queryAllToTree();
            if( null == itemCatResult ) {

                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);

            }

            return ResponseEntity.status(HttpStatus.OK).body(itemCatResult);

        } catch ( Exception e ) {

            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }

    }

}
