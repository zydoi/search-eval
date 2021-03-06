package com.litb.search.eval.dto.solr;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.beans.Field;

public class SolrItemDTO {

	@Field
	private String id;
	
	@Field("name_en")
	private String name;

	@Field("in_stock")
	private boolean inStock;
	
	@Field
	private int brand;
	
	@Field
	private int manufacturer;
	
	@Field
	private double price;

	@Field("category_en")
	private String category;
	
	@Field("fav_num")
	private int favNum;
	
	@Field("new_effective_value")
	private double newEffectiveValue;
	
	@Field("effective_value")
	private double effectiveValue;
	
	@Field("effective_continent_1")
	private double effectiveContinent1;
	
	@Field("sale_amount")
	private int saleAmount;
	
	@Field("sku_en")
	private String sku;
	
	@Field("description_en")
	private String description;

	@Field("last_category_en")
	private String lastCategory;
	
	@Field("add_at")
	private Date addAt;
	
	@Field("available_stocks")
	private List<String> availableStocks;
	
	@Field("master_category_id")
	private List<String> masterCategoryIDs;
	
	@Field("sku_attributes")
	private List<String> skuAttributes;
	
	@Field("query_*")
	private Map<String, Integer> queries;

	public String getName() {
		return name;
	}
	
	public int getQuery(String qid) {
		if(queries == null) {
			return 0;
		}
		int result = queries.get(qid) == null? 0: queries.get(qid);
		return result;
	}
	
	public Map<String, Integer> getQueries() {
		return queries;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getFavNum() {
		return favNum;
	}

	public void setFavNum(int favNum) {
		this.favNum = favNum;
	}

	public String getLastCategory() {
		return lastCategory;
	}

	public void setLastCategory(String lastCategory) {
		this.lastCategory = lastCategory;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}
}
