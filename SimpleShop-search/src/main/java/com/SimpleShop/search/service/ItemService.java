package com.SimpleShop.search.service;

import com.SimpleShop.commons.util.httpClient.ApiContentService;
import com.SimpleShop.commons.util.httpClient.HttpClientResult;
import com.SimpleShop.search.entiy.Item;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.HttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class ItemService {

    @Autowired(required = false)
    public ApiContentService apiContentService;

    public static final ObjectMapper MAPPER = new ObjectMapper();

    public Item selectItemById(Long id) throws Exception {

        Item item = new Item();
        String result = apiContentService.doGet("http://manage.simpleshop.com/rest/api/item/"+id);
        if(result.isEmpty()){

            throw new Exception("无法获取item数据！");
        }

        JsonNode node = MAPPER.readTree(result);
        item.setId(node.get("id").asLong());
        item.setTitle(node.get("title").asText());
        item.setImage(node.get("image").asText());
        item.setPrice(node.get("price").asLong());
        item.setCid(node.get("cid").asLong());
        item.setSellPoint(node.get("sellPoint").asText());
        item.setStatus(node.get("status").asInt());
        item.setUpdated(node.get("updated").asLong());

        return item;
    }

}
