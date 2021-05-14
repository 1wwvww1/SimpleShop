package com.SimpleShop.manage.service;

import com.SimpleShop.commons.EasyUIResult;
import com.SimpleShop.manage.entiy.Item;
import com.SimpleShop.manage.entiy.ItemDesc;
import com.SimpleShop.manage.entiy.ItemParamItem;
import com.SimpleShop.manage.mapper.ItemMapper;
import com.SimpleShop.manage.mapper.ItemParamItemMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.abel533.entity.Example;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.rabbitmq.client.impl.AMQConnection;
import com.sun.javafx.collections.MappingChange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ItemService extends BaseService<Item>{

    @Autowired
    public ItemDescService itemDescService;

    @Autowired
    public ItemParamItemMapper itemParamItemMapper;

    @Autowired
    public ItemParamItemService itemParamItemService;

    @Autowired
    public RabbitTemplate rabbitTemplate;

    private static ObjectMapper mapper = new ObjectMapper();

    private void sendMessage(Item item,String type) throws JsonProcessingException {

        Map<String,Object> itemMap = new HashMap<>();
        itemMap.put("id",item.getId());
        itemMap.put("time",item.getUpdated());
        itemMap.put("images",item.getImage());
        itemMap.put("title",item.getTitle());

        rabbitTemplate.convertAndSend("item."+type,mapper.writeValueAsString(itemMap));
    }

    public void addItem(Item item, ItemParamItem itemParamItem, String desc) {

        item.setId(null);
        item.setStatus(1);

        super.save(item);

        ItemDesc itemDesc = new ItemDesc();
        itemDesc.setItemId(item.getId());
        itemDesc.setItemDesc(desc);
        this.itemDescService.save(itemDesc);

        itemParamItem.setId(null);
        itemParamItem.setItemId(item.getId());
        itemParamItemService.save(itemParamItem);

    }

    public EasyUIResult queryItemList(Integer page, Integer rows) {

        PageHelper.startPage(page,rows);
        Example example = new Example(Item.class);
        example.setOrderByClause("updated DESC");

        List<Item> items = super.mapper.selectByExample(example);
        PageInfo<Item> pageInfo = new PageInfo<Item>(items);

        return new EasyUIResult(pageInfo.getTotal(),pageInfo.getList());

    }

    public boolean updateItem(Item item,ItemParamItem itemParamItem,String desc) {

        //item.setId(null);
        item.setCreated(null);
        item.setStatus(null);
        item.setUpdated(new Date());
        int isSucess = super.updateSelective(item);

        if (isSucess == 0) {
            return false;
        }

        ItemDesc itemDesc = new ItemDesc();
        itemDesc.setUpdated(new Date());
        itemDesc.setItemId(item.getId());
        itemDesc.setItemDesc(desc);
        this.itemDescService.updateSelective(itemDesc);

        itemParamItem.setId(null);
        Example example = new Example(ItemParamItem.class);
        example.createCriteria().andEqualTo("itemId",itemParamItem.getItemId());
        this.itemParamItemMapper.updateByExample(itemParamItem,example);

        try {

            sendMessage(item,"update");
        } catch (Exception e){

            e.printStackTrace();
        }

        return true;
    }

    public boolean deleteItemById( List<Object> ids ) {

        int isSucess =  super.deleteByIds(Item.class,"id",ids);
        if( isSucess == 0 ) {
            return false;
        }

        this.itemDescService.deleteByIds(ItemDesc.class,"itemId",ids);

        return true;

    }


}
