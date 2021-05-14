package com.SimpleShop.web.service;

import com.SimpleShop.commons.util.httpClient.ApiContentService;

import com.SimpleShop.commons.util.redis.RedisPoolSevice;
import com.SimpleShop.manage.entiy.ItemDesc;
import com.SimpleShop.web.entiy.Item;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
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

    public ItemDesc queryItemDescByItemId(Long itemId) {

        try {

            String data = redisPoolSevice.getKey(REDIS_ITEM_SUF+"DESC_"+itemId);
            if ( !StringUtils.isEmpty(data) ) {
                return MAPPER.readValue(data,ItemDesc.class);
            }
        } catch ( Exception e ) {

            e.printStackTrace();
        }

        try {
            String data = apiContentService.doGet(MANAGER_API_URL+"/rest/api/item/desc/"+itemId);
            if( StringUtils.isEmpty(data)  ) {
                return null;
            }
            try {
                redisPoolSevice.set(REDIS_ITEM_SUF+"DESC_"+itemId,data,WEB_ITEM_PERSIST_TIME);
            } catch ( Exception e ) {
                e.printStackTrace();
            }
            return MAPPER.readValue(data, ItemDesc.class);
        } catch ( Exception e ) {
            e.printStackTrace();
        }

        return null;
    }

    public String queryItemParamItemByItemId(Long itemId) {

        try {
            String data = redisPoolSevice.getKey(REDIS_ITEM_SUF+"PARAM_ITEM"+itemId);
            if ( !StringUtils.isEmpty(data) ) {
                return data;
            }
        } catch ( Exception e ) {
            e.printStackTrace();
        }

        try {
            String data = apiContentService.doGet(MANAGER_API_URL+"/rest/api/item/param/item/"+itemId);
            if( StringUtils.isEmpty(data)  ) {
                return null;
            }

            JsonNode jsonNode = MAPPER.readTree(data);
            String paramData = jsonNode.get("paramData").asText();
            if( !StringUtils.isEmpty(paramData) ) {
                ArrayNode arrayNode = (ArrayNode) MAPPER.readTree(paramData);
                StringBuilder stringBuilder = new StringBuilder();

                //添加包装前准备
                stringBuilder.append("<div id=\"container_info\" class=\"clearfix\"><div class=\"info-box\" id=\"box_configuration\"><div class=\"padding-top\"></div><div class=\"con\">");

                for ( JsonNode group : arrayNode ) {

                    //包装group
                    stringBuilder.append("<div class=\"good_item\"><div class=\"item-title-container\"><div class=\"item_title\">"+group.get("group").asText()+"</div><div class=\"item_title1\"></div></div>");

                    //包装keys values
                    ArrayNode params = (ArrayNode) group.get("params");
                    for ( JsonNode param : params ) {

                        stringBuilder.append("<div class=\"item_row\"><div class=\"col_one\">"+param.get("k").asText()+"</div><div class=\"col_one col_values col_two1\">"+param.get("v").asText()+"</div>");
                    }

                    //结束包装group key value
                    stringBuilder.append("<div class=\"item_row item-row-last\"><div class=\"col_one\"></div><div class=\"col_one col_values col_two1\"></div></div></div>");
                }

                //结束外层包装
                stringBuilder.append("</div> </div></div>");

                try {
                    redisPoolSevice.set(REDIS_ITEM_SUF+"PARAM_ITEM"+itemId,stringBuilder.toString(),WEB_ITEM_PERSIST_TIME);
                } catch ( Exception e ) {
                    e.printStackTrace();
                }

                return stringBuilder.toString();
            }
        } catch ( Exception e ) {
            e.printStackTrace();
        }

        return null;
    }

}
