package com.SimpleShop.cart.service;

import com.SimpleShop.cart.bean.Item;
import com.SimpleShop.commons.util.httpClient.ApiContentService;
import com.SimpleShop.commons.util.redis.RedisPoolSevice;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ItemService {

    @Autowired(required = false)
    public ApiContentService apiContentService;

    @Autowired(required = false)
    public RedisPoolSevice redisPoolSevice;

    @Value("${MANAGER.API.URL}")
    private String MANAGER_API_URL;

    @Value("${WEB.ITEM.PERSIST.TIME}")
    private Integer WEB_ITEM_PERSIST_TIME;

    public String REDIS_ITEM_SUF="SIMPLESHOP_WEB_ITEM_";

    private static final ObjectMapper MAPPER = new ObjectMapper();

    public Item queryItemById(Long itemId) {

        try {

            String data = redisPoolSevice.getKey(REDIS_ITEM_SUF+itemId);
            if ( !StringUtils.isEmpty(data) ) {
                return MAPPER.readValue(data,Item.class);
            }
        } catch ( Exception e ) {

            e.printStackTrace();
        }

        try{

            String url=MANAGER_API_URL+"/rest/api/item/"+itemId;
            String data = apiContentService.doGet(url);
            try {
                redisPoolSevice.set(REDIS_ITEM_SUF+itemId,data,WEB_ITEM_PERSIST_TIME);
            } catch ( Exception e ) {
                e.printStackTrace();
            }
            return MAPPER.readValue(data,Item.class);
        } catch ( Exception e ) {

            e.printStackTrace();
        }

        return null;
    }

}
