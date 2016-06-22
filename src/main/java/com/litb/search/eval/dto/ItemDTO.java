package com.litb.search.eval.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ItemDTO {
	
	@JsonProperty("item_id")
	private String itemId;
	
	private String currency;
	
	private String masterCategoryId;
	
	@JsonProperty("review_rating")
	private int reviewRating;
	
	@JsonProperty("item_url")
	private String itemURL;
	
	@JsonProperty("main_img_url")
	private String mainImgURL;
	
	private double originalPrice;
	
	private double salePrice;
	
	@JsonProperty("item_name")
	private String itemName;
	
	@JsonProperty("favorite_times")
	private int favoriteTimes;

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getMasterCategoryId() {
		return masterCategoryId;
	}

	public void setMasterCategoryId(String masterCategoryId) {
		this.masterCategoryId = masterCategoryId;
	}

	public int getReviewRating() {
		return reviewRating;
	}

	public void setReviewRating(int reviewRating) {
		this.reviewRating = reviewRating;
	}

	public String getItemURL() {
		return itemURL;
	}

	public void setItemURL(String itemURL) {
		this.itemURL = itemURL;
	}

	public String getMainImgURL() {
		return mainImgURL;
	}

	public void setMainImgURL(String mainImgURL) {
		this.mainImgURL = mainImgURL;
	}

	public double getOriginalPrice() {
		return originalPrice;
	}

	public void setOriginalPrice(double originalPrice) {
		this.originalPrice = originalPrice;
	}

	public double getSalePrice() {
		return salePrice;
	}

	public void setSalePrice(double salePrice) {
		this.salePrice = salePrice;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public int getFavoriteTimes() {
		return favoriteTimes;
	}

	public void setFavoriteTimes(int favoriteTimes) {
		this.favoriteTimes = favoriteTimes;
	}
}