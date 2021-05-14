package com.SimpleShop.search.controller;

import com.SimpleShop.search.bean.SearchResult;
import com.SimpleShop.search.entiy.Item;
import com.SimpleShop.search.service.SearchService;
import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;

@Controller
public class SearchController {

    @Autowired
    public SearchService searchService;

    int ROWS=10;

    @RequestMapping("search")
    public ModelAndView selectItem(@RequestParam("q") String keyWords,
       @RequestParam(value = "page", defaultValue = "1") Integer page) {

        ModelAndView mv = new ModelAndView("search");
        SearchResult searchResult = null;
        searchResult = this.searchService.selectItem(keyWords, page, ROWS);

        // 搜索关键字
        mv.addObject("query", keyWords);

        // 搜索结果集
        mv.addObject("itemList", searchResult.getItemList());

        // 当前页数
        mv.addObject("page", page);

        // 总页数
        int total = searchResult.getTotal().intValue();
        int pages = total % ROWS == 0 ? total / ROWS : total / ROWS + 1;
        mv.addObject("pages", pages);

        return mv;
    }
}
