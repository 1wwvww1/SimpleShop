package com.SimpleShop.manage.entiy;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ItemCatData  {
	
	@JsonProperty("u") // 序列化成json数据时为 u
	private String url;
	
	@JsonProperty("n")
	private String nname;
	
	@JsonProperty("i")
	private List<?> items;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getName() {
		return nname;
	}

	public void setName(String nname) {
		this.nname = nname;
	}

	public List<?> getItems() {
		return items;
	}

	public void setItems(List<?> items) {
		this.items = items;
	}
	
	

}
