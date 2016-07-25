package com.litb.search.eval.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity(name = "item")
public class EvalItem {

	@Id
	private String id;
	
	@Column(nullable = false)
	private String name;
	
	@Column(nullable = false)
	private Date addedTime;
	
	@Column
	private String lastCategory;
	
	@Column
	private String itemURL;
	
	@Column
	private String imageURL;
	
	@Column
	private int favNum;

	@Column
	private double price;
	
	@OneToMany(mappedBy = "item", cascade = CascadeType.ALL)
	private Set<EvalItemAnnotation> itemAnnotations = new HashSet<>();
	
	public EvalItem() {
	}
	
	public EvalItem(String id, String name) {
		this.id = id;
		this.name = name;
		this.addedTime = new Date();
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getAddedTime() {
		return addedTime;
	}

	public void setAddedTime(Date addedTime) {
		this.addedTime = addedTime;
	}

	public String getLastCategory() {
		return lastCategory;
	}

	public void setLastCategory(String lastCategory) {
		this.lastCategory = lastCategory;
	}

	public int getFavNum() {
		return favNum;
	}

	public void setFavNum(int favNum) {
		this.favNum = favNum;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getItemURL() {
		return itemURL;
	}

	public void setItemURL(String itemURL) {
		this.itemURL = itemURL;
	}

	public String getImageURL() {
		return imageURL;
	}

	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}

	public Set<EvalItemAnnotation> getItemAnnotations() {
		return itemAnnotations;
	}

	public void setItemAnnotations(Set<EvalItemAnnotation> itemAnnotations) {
		this.itemAnnotations = itemAnnotations;
	}
	
}
