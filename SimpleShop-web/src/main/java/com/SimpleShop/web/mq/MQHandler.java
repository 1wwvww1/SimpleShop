package com.SimpleShop.web.mq;

import com.SimpleShop.commons.util.redis.RedisPoolSevice;
import com.SimpleShop.web.service.ItemService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

public class MQHandler {

    @Autowired
    public RedisPoolSevice redisPoolSevice;

    @Autowired
    public ItemService itemService;

    private static ObjectMapper mapper = new ObjectMapper();

    public void itemHandle(String message) {

        try {

            JsonNode jsonNode = mapper.readTree(message);
            Long id = jsonNode.get("id").asLong();
            redisPoolSevice.delKey(itemService.REDIS_ITEM_SUF+id);
        }catch (Exception e){

            e.printStackTrace();
        }


    }
}
