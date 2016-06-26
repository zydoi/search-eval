package com.litb.search.eval.dto;

import java.util.Set;

public class AnnotateDTO {

	private String annotator;
	
	private String queryID;


	private Set<Integer> pids;

	public AnnotateDTO() {}
	
	public AnnotateDTO(String annotator, String queryID) {
		this.annotator = annotator;
		this.queryID = queryID;
	}
	
	public String getAnnotator() {
		return annotator;
	}

	public void setAnnotator(String annotator) {
		this.annotator = annotator;
	}

	public Set<Integer> getPids() {
		return pids;
	}

	public void setPids(Set<Integer> pids) {
		this.pids = pids;
	}
	
	public String getQueryID() {
		return queryID;
	}

	public void setQueryID(String queryID) {
		this.queryID = queryID;
	}
}
