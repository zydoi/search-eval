package com.litb.search.eval.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LitbInfoDTO {
	
	@JsonProperty("products_list")
	private List<String> productsList;
	
	@JsonProperty("items")
	private List<ItemDTO> items;

	public List<String> getProductsList() {
		return productsList;
	}

	public void setProductsList(List<String> productsList) {
		this.productsList = productsList;
	}

	public List<ItemDTO> getItems() {
		return items;
	}

	public void setItems(List<ItemDTO> items) {
		this.items = items;
	}
}
