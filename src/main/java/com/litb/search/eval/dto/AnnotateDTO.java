package com.litb.search.eval.dto;

import java.util.Set;

public class AnnotateDTO {

	private String annotator;
	
	private String query;
	
	private Set<Integer> pids;

	public String getAnnotator() {
		return annotator;
	}

	public void setAnnotator(String annotator) {
		this.annotator = annotator;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public Set<Integer> getPids() {
		return pids;
	}

	public void setPids(Set<Integer> pids) {
		this.pids = pids;
	}
}
