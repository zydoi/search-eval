package com.litb.search.eval.dto;

import java.util.Set;

import com.litb.search.eval.entity.EvalQuery;
import com.litb.search.eval.repository.QueryType;

public class QueryDTO {

	private int id;
	
	private String name;
	
	private Set<QueryType> types;
	
	private boolean effective;
	
	public QueryDTO() {}
	
	public QueryDTO(EvalQuery query) {
		this.id = query.getId();
		this.name = query.getName();
		this.types = query.getQueryTypes();
		this.effective = query.isEffective();
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<QueryType> getTypes() {
		return types;
	}

	public void setTypes(Set<QueryType> types) {
		this.types = types;
	}

	public boolean isEffective() {
		return effective;
	}

	public void setEffective(boolean effective) {
		this.effective = effective;
	}
}
