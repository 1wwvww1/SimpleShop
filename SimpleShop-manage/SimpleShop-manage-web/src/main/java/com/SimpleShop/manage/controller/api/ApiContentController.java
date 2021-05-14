package com.SimpleShop.manage.controller.api;

import com.SimpleShop.commons.EasyUIResult;
import com.SimpleShop.manage.entiy.Content;
import com.SimpleShop.manage.service.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.List;

@RequestMapping("/api/content")
@Controller
public class ApiContentController {

    @Autowired
    public ContentService contentService;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<EasyUIResult> queryContentList(
            @RequestParam(value = "rows", defaultValue = "6", required = false) Integer rows,
            @RequestParam(value = "page", defaultValue = "1", required = false) Integer page, Content content) {
        try {

            EasyUIResult easyUIResult = contentService.queryContentByCategoryId(content, rows, page);
            if( null == easyUIResult ) {

                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            return ResponseEntity.status(HttpStatus.OK).body(easyUIResult);
        } catch (Exception e) {
            e.printStackTrace();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }

    }

}
