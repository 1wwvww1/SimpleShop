package com.SimpleShop.web.service;

import com.SimpleShop.commons.EasyUIResult;
import com.SimpleShop.commons.util.httpClient.ApiContentService;
import com.SimpleShop.manage.entiy.Content;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class IndexService {

    @Autowired(required = false)
    public ApiContentService apiContentService;

    @Value("${MANAGER.API.URL}")
    public String MANAGER_API_URL;

    @Value("${INDEX.AD1.URI}")
    public String INDEX_AD1_URI;

    @Value("${INDEX.AD2.URI}")
    public String INDEX_AD2_URI;

    @Value("${INDEX.AD3.URI}")
    public String INDEX_AD3_URI;

    public static final ObjectMapper MAPPER = new ObjectMapper();

    public String  queryIndexAD1() {
        String url=MANAGER_API_URL+INDEX_AD1_URI;
        try {
            String data = apiContentService.doGet(url);
            if( null != data ) {
                EasyUIResult easyUIResult = EasyUIResult.formatToList(data, Content.class);
                List<Content> contents = (List<Content>) easyUIResult.getRows();

                List<Map<String,Object>> res = new ArrayList<Map<String,Object>>();
                for (Content content : contents) {
                    Map<String,Object> map = new LinkedHashMap<String,Object>();
                    map.put("src",content.getPic());
                    map.put("srcB",content.getPic2());
                    map.put("href",content.getUrl());
                    map.put("alt",content.getTitle());
                    map.put("height",240);
                    map.put("width",670);
                    map.put("widthB",550);
                    map.put("heightB",240);
                    res.add(map);
                }
                return MAPPER.writeValueAsString(res);
            }
        } catch ( Exception e) {
            e.printStackTrace();
        }

        return null;

    }

    public String  queryIndexAD2() {
        String url=MANAGER_API_URL+INDEX_AD2_URI;
        try {
            String data = apiContentService.doGet(url);
            if( null != data ) {
                EasyUIResult easyUIResult = EasyUIResult.formatToList(data, Content.class);
                List<Content> contents = (List<Content>) easyUIResult.getRows();
                //contents.sort((c1,c2)->{return c1.getId()>c2.getId()?1:0;});

                List<Map<String,Object>> res = new ArrayList<Map<String,Object>>();
                for (Content content : contents) {
                    Map<String,Object> map = new LinkedHashMap<String,Object>();
                    map.put("height",70);
                    map.put("width",310);
                    map.put("src",content.getPic());
                    map.put("srcB",content.getPic2());
                    map.put("href",content.getUrl());
                    map.put("alt",content.getTitle());
                    map.put("widthB",210);
                    map.put("heightB",70);
                    res.add(map);
                }
                return MAPPER.writeValueAsString(res);
            }
        } catch ( Exception e) {
            e.printStackTrace();
        }

        return null;

    }

    public String  queryIndexAD3() {

        String url=MANAGER_API_URL+INDEX_AD3_URI;
        try {
            String data = apiContentService.doGet(url);
            if( null != data ) {
                EasyUIResult easyUIResult = EasyUIResult.formatToList(data, Content.class);
                List<Content> contents = (List<Content>) easyUIResult.getRows();
                //contents.sort((c1,c2)->{return c1.getId()>c2.getId()?1:0;});

                List<Map<String,Object>> res = new ArrayList<Map<String,Object>>();
                for (Content content : contents) {
                    Map<String,Object> map = new LinkedHashMap<String,Object>();
                    map.put("title",content.getTitle());
                    map.put("href",content.getUrl());
                    map.put("alt",content.getTitle());
                    res.add(map);
                }
                return MAPPER.writeValueAsString(res);
            }
        } catch ( Exception e) {
            e.printStackTrace();
        }

        return null;

    }

}
