package com.SimpleShop.manage.controller;

import com.SimpleShop.manage.entiy.ContentCategory;
import com.SimpleShop.manage.service.ContentCategoryService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.ArrayList;
import java.util.List;

/**
 * @author YGH
 */
@RequestMapping("content/category")
@Controller
public class ContentCategoryController {

    @Autowired
    public ContentCategoryService contentCategoryService;

    //通过ID查询子字段
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<ContentCategory>> queryContentCategoryByParentId(@RequestParam(value = "id", defaultValue = "0") Long parentId) {
        try {
            List<ContentCategory> result = new ArrayList<ContentCategory>();
            result = contentCategoryService.queryContentCategoryByParentId(parentId);
            //System.out.println(result + "parentId" +parentId );
            if (0 == result.size() || null == result) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<ContentCategory> saveContentCategory(ContentCategory contentCategory) {
        System.out.println(contentCategory);
        try {
            ContentCategory result = contentCategoryService.saveContentCategory(contentCategory);

            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<Void> updateContentCategory(ContentCategory contentCategory) {
        try {
            contentCategory.setIsParent(null);
            contentCategoryService.updateContentCategory(contentCategory);

            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteContentCategory(ContentCategory contentCategory) {
        try {
            contentCategoryService.deleteContentCategory(contentCategory);

            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

}
