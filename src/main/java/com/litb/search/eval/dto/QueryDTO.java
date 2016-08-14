package com.litb.search.eval.dto;

import java.util.List;

import com.litb.search.eval.repository.QueryType;

public class QueryDTO {

	private String id;
	
	private String name;
	
	private List<QueryType> types;
	
	private boolean effective;
	
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

	public List<QueryType> getTypes() {
		return types;
	}

	public void setTypes(List<QueryType> types) {
		this.types = types;
	}

	public boolean isEffective() {
		return effective;
	}

	public void setEffective(boolean effective) {
		this.effective = effective;
	}
}
