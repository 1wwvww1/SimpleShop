package com.SimpleShop.manage.service;

import com.SimpleShop.commons.EasyUIResult;
import com.SimpleShop.manage.entiy.ItemParam;
import com.SimpleShop.manage.mapper.ItemParamMapper;
import com.github.abel533.entity.Example;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemParamService extends BaseService<ItemParam> {

    @Autowired
    public ItemParamMapper itemParamMapper;

    public EasyUIResult queryItemParam(Integer rows,Integer page) {

        PageHelper.startPage(page,rows);
        List<ItemParam> itemParams = super.queryAll();
        PageInfo<ItemParam> itemParamPageInfo = new PageInfo<ItemParam>(itemParams);

        EasyUIResult easyUIResult = new EasyUIResult();
        easyUIResult.setRows(itemParamPageInfo.getList());
        easyUIResult.setTotal((int) itemParamPageInfo.getTotal());

        return easyUIResult;

    }

    public ItemParam queryByItemCatId(Long itemCatId) {


        Example example = new Example(ItemParam.class);
        example.createCriteria().andEqualTo("itemCatId",itemCatId);
        List<ItemParam> itemParams=null;
        itemParams = itemParamMapper.selectByExample(example);
        if( null == itemParams || itemParams.size()<1 ){

            return null;
        } else {

            return itemParams.get(0);
        }

    }


}
