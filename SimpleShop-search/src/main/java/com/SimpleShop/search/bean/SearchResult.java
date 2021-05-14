package com.SimpleShop.search.bean;

import com.SimpleShop.search.entiy.Item;

import java.util.List;

public class SearchResult {

    public SearchResult(){}

    public SearchResult(Long total, List<Item> itemList) {
        this.total = total;
        this.itemList = itemList;
    }

    private Long total;
    private List<Item> itemList;

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List<Item> getItemList() {
        return itemList;
    }

    public void setItemList(List<Item> itemList) {
        this.itemList = itemList;
    }
}
