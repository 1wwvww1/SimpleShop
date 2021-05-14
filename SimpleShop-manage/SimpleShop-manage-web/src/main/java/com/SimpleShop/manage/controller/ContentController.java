package com.SimpleShop.manage.controller;

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

import javax.xml.crypto.Data;
import java.util.Date;
import java.util.List;

@RequestMapping("content")
@Controller
public class ContentController {

    @Autowired
    public ContentService contentService;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<EasyUIResult> queryContentList(
            @RequestParam(value = "rows", defaultValue = "20", required = false) Integer rows,
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

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Void> saveContent(Content content) {
        try {

            contentService.saveContent(content);
            return ResponseEntity.status(HttpStatus.OK).build();

        } catch (Exception e) {
            e.printStackTrace();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }


    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<Void> updateContent(Content content) {
        try {
            if( null == content.getId() ) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            content.setUpdated(new Date());
            contentService.updateSelective(content);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

        } catch (Exception e) {
            e.printStackTrace();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    @RequestMapping(method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteContent(@RequestParam("ids")List<Object> ids) {
        //System.out.println(ids.size());
        try {
            if( null == ids ) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            contentService.deleteByIds(Content.class,"id",ids);
            return ResponseEntity.status(HttpStatus.OK).build();

        } catch (Exception e) {
            e.printStackTrace();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }


}
