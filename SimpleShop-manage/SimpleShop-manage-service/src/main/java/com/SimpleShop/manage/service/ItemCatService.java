package com.SimpleShop.manage.service;

import com.SimpleShop.commons.EasyUIResult;
import com.SimpleShop.manage.entiy.ItemCat;
import com.SimpleShop.manage.entiy.ItemCatData;
import com.SimpleShop.manage.entiy.ItemCatResult;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ItemCatService extends BaseService<ItemCat>{


    public List<ItemCat> queryItemCatByParentId( long id ) {

        List<ItemCat> itemCats =null;

        ItemCat recond =  new ItemCat();

        recond.setParentId(id);

        itemCats = this.queryListByWhere( recond );

        return itemCats;

    }


    public ItemCatResult queryAllToTree() {
        ItemCatResult result = new ItemCatResult();
        // 全部查出，并且在内存中生成树形结构
        List<ItemCat> cats = super.queryAll();

        // 转为map存储，key为父节点ID，value为数据集合
        Map<Long, List<ItemCat>> itemCatMap = new HashMap<Long, List<ItemCat>>();
        for (ItemCat itemCat : cats) {
            if (!itemCatMap.containsKey(itemCat.getParentId())) {
                itemCatMap.put(itemCat.getParentId(), new ArrayList<ItemCat>());
            }
            itemCatMap.get(itemCat.getParentId()).add(itemCat);
        }

        // 封装一级对象
        List<ItemCat> itemCatList1 = itemCatMap.get(0L);
        for (ItemCat itemCat : itemCatList1) {
            ItemCatData itemCatData = new ItemCatData();
            itemCatData.setUrl("/products/" + itemCat.getId() + ".html");
            itemCatData.setName("<a href='" + itemCatData.getUrl() + "'>" + itemCat.getName() + "</a>");
            result.getItemCats().add(itemCatData);
            if (!itemCat.getIsParent()) {
                continue;
            }

            // 封装二级对象
            List<ItemCat> itemCatList2 = itemCatMap.get(itemCat.getId());
            List<ItemCatData> itemCatDataLV2 = new ArrayList<ItemCatData>();
            itemCatData.setItems(itemCatDataLV2);
            for (ItemCat itemCat2 : itemCatList2) {
                ItemCatData id2 = new ItemCatData();
                id2.setName(itemCat2.getName());
                id2.setUrl("/products/" + itemCat2.getId() + ".html");
                itemCatDataLV2.add(id2);
                if (itemCat2.getIsParent()) {
                    // 封装三级对象
                    List<ItemCat> itemCatList3 = itemCatMap.get(itemCat2.getId());
                    List<String> itemCatData3 = new ArrayList<String>();
                    id2.setItems(itemCatData3);
                    for (ItemCat itemCat3 : itemCatList3) {
                        itemCatData3.add("/products/" + itemCat3.getId() + ".html|" + itemCat3.getName());
                    }
                }
            }
            if (result.getItemCats().size() >= 14) {
                break;
            }
        }
        return result;
    }

/*    public ItemCatResult queryAllToTree1() {

        ItemCatResult itemCatResult = new ItemCatResult();
        List<ItemCat> cats = super.queryAll();
        if( null == cats ) {
            return null;
        }

        //封装MAP{parentId:itemCat}
        Map<Long,List<ItemCat>> map = new HashMap<Long,List<ItemCat>>();
        for( ItemCat itemCat : cats ) {
            if( !map.containsKey(itemCat.getParentId()) ) {
                List list = new ArrayList<ItemCat>().add(itemCat)
                map.put(itemCat.getParentId(),list);
            }else {
                map.get(itemCat.getParentId()).add(itemCat);
            }
        }

        *//*
         "u": "/products/1.html",
         "n": "<a href='/products/1.html'>图书、音像、电子书刊</a>",
         "i":
         *//*
        //封装一级目录；
        List<ItemCat> itemCats1 = map.get(0L);
        for ( ItemCat itemCat1 : itemCats1 ){
            ItemCatData itemCatDataLV1 = new ItemCatData();
            itemCatDataLV1.setUrl("/products/"+itemCat1.getId()+".html");
            itemCatDataLV1.setName("<a href=\'"+itemCatDataLV1.getUrl()+"\'>"+itemCat1.getName()+"</a>");
            itemCatResult.getItemCats().add(itemCatDataLV1);
            if( !itemCat1.getIsParent() ){
                continue;
            }
            List<ItemCat> itemCats2 = map.get(itemCat1.getParentId());
            itemCatDataLV1.setItems(itemCats2);

            //封装二级目录
            for( ItemCat itemCat2 : itemCats2 ) {
                ItemCatData itemCatDataLV2 = new ItemCatData();
                itemCatDataLV2.setUrl("/products/"+itemCat2.getId()+".html");
                itemCatDataLV2.setName("<a href=\'"+itemCatDataLV2.getUrl()+"\'>"+itemCat2.getName()+"</a>");

                //封装三级目录
                if( itemCat2.getIsParent() ){
                    List<ItemCat> itemCats3 = map.get(itemCat2.getParentId());
                    List<String> itemCats3List = new ArrayList<String>();
                    for( ItemCat itemCat3 : itemCats3 ){
                        itemCats3List.add("/products/"+itemCat3.getId()+".html|"+itemCat3.getName())
                    }
                    itemCatDataLV2.setItems(itemCats3List);
                }
            }

            if ( itemCatResult.getItemCats().size()>14 ){
                break;
            }

        }

        return null;

    }*/

}
