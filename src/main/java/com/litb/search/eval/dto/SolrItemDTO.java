package com.litb.search.eval.dto;

import java.util.List;

import org.apache.solr.client.solrj.beans.Field;

public class SolrItemDTO {

	@Field
	private String id;
	
	@Field
	private String name;
	
	@Field
	private List<String> feature;

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

	public List<String> getFeature() {
		return feature;
	}

	public void setFeature(List<String> feature) {
		this.feature = feature;
	}
	
}
