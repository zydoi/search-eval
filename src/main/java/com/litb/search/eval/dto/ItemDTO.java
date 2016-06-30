package com.litb.search.eval.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ItemDTO {
	
	@JsonIgnoreProperties(ignoreUnknown = true)
	static class CateShowImgs {
		
		private Grid grid;

		public Grid getGrid() {
			return grid;
		}

		public void setGrid(Grid grid) {
			this.grid = grid;
		}
	}
	
	@JsonIgnoreProperties(ignoreUnknown = true)
	static class Grid {
		
		private String rectangle;
		
		private String square;

		public String getRectangle() {
			return rectangle;
		}

		public void setRectangle(String rectangle) {
			this.rectangle = rectangle;
		}

		public String getSquare() {
			return square;
		}

		public void setSquare(String square) {
			this.square = square;
		}
	}
	
	@JsonProperty("cate_show_imgs")
	private CateShowImgs cateShowImgs;
	
	@JsonProperty("item_id")
	private String itemId;
	
	private boolean isNew;
	
	private String currency;
	
	private String masterCategoryId;

	private String masterCategoryName;
	
	@JsonProperty("review_rating")
	private int reviewRating;
	
	@JsonProperty("item_url")
	private String itemURL;
	
	@JsonProperty("main_img_url")
	private String mainImgURL;
	
	@JsonProperty("rectangle")
	private String showImgURL;
	
	private double originalPrice;
	
	@JsonProperty("sale_price")
	private double salePrice;
	
	@JsonProperty("item_name")
	private String itemName;
	
	@JsonProperty("review_count")
	private int reviewCount;
	
	@JsonProperty("favorite_times")
	private int favoriteTimes;

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public int getReviewCount() {
		return reviewCount;
	}

	public void setReviewCount(int reviewCount) {
		this.reviewCount = reviewCount;
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

	public CateShowImgs getCateShowImgs() {
		return cateShowImgs;
	}

	public void setCateShowImgs(CateShowImgs cateShowImgs) {
		this.cateShowImgs = cateShowImgs;
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

	public String getMasterCategoryName() {
		return masterCategoryName;
	}

	public void setMasterCategoryName(String masterCategoryName) {
		this.masterCategoryName = masterCategoryName;
	}

	public String getShowImgURL() {
		return showImgURL;
	}

	public void setShowImgURL(String showImgURL) {
		this.showImgURL = showImgURL;
	}

	public boolean isNew() {
		return isNew;
	}

	public void setNew(boolean isNew) {
		this.isNew = isNew;
	}
}
