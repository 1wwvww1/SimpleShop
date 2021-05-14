package com.SimpleShop.manage.entiy;

import java.util.ArrayList;
import java.util.List;

import com.SimpleShop.manage.entiy.ItemCatData;
import com.fasterxml.jackson.annotation.JsonProperty;


public class ItemCatResult {

	@JsonProperty("data")
	private List<ItemCatData> itemCats = new ArrayList<ItemCatData>();

	public List<ItemCatData> getItemCats() {
		return itemCats;
	}

	public void setItemCats(List<ItemCatData> itemCats) {
		this.itemCats = itemCats;
	}

}
