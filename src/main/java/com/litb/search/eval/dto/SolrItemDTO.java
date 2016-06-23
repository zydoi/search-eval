package com.litb.search.eval.dto;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.beans.Field;

public class SolrItemDTO {

	@Field
	private String id;
	
	@Field
	private String name;

	@Field("in_stock")
	private boolean inStock;
	
	@Field
	private int brand;
	
	@Field
	private int manufacturer;
	
	@Field
	private double price;
	
	@Field
	private List<String> category;
	
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
	private String skuEn;
	
	@Field("description_en")
	private String descriptionEn;

	@Field("last_category_en")
	private String lastCategoryEn;
	
	@Field("add_at")
	private Date addAt;
	
	@Field("available_stocks")
	private List<String> availableStocks;
	
	@Field("master_category_id")
	private List<String> masterCategoryIDs;
	
	@Field("sku_attributes")
	private List<String> skuAttributes;
	
	@Field("query_*")
	private Map<String, String> queries;

	public String getName() {
		return name;
	}
	
	public String getQuery(String qid) {
		return queries.get(qid);
	}
	
}
