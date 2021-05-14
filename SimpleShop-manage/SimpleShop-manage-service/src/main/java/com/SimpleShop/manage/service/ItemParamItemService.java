package com.SimpleShop.manage.service;

import com.SimpleShop.manage.entiy.ItemParamItem;
import com.SimpleShop.manage.mapper.ItemParamItemMapper;
import com.github.abel533.entity.Example;
import jdk.nashorn.internal.ir.ReturnNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemParamItemService extends BaseService<ItemParamItem> {

    @Autowired
    public ItemParamItemMapper itemParamItemMapper;

    public ItemParamItem queryByItemId(Long itemId) {

        Example example = new Example(ItemParamItem.class);
        example.createCriteria().andEqualTo ("itemId",itemId);
        List<ItemParamItem> itemParamItems = itemParamItemMapper.selectByExample(example);
        if( null == itemParamItems.get(0) ) {
            return null;
        }

        return itemParamItems.get(0);

    }


}
