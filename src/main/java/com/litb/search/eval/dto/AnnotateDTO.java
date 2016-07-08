package com.litb.search.eval.dto;

import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

public class AnnotateDTO {

	private String annotator;
	
	private int queryID;

	private Set<String> relevantPids;
	
	private Set<String> pids;
	
	public AnnotateDTO() {}
	
	public AnnotateDTO(String annotator, int queryID, Collection<String> pids) {
		this.annotator = annotator;
		this.queryID = queryID;
		this.pids = new TreeSet<>();
		this.pids.addAll(pids);
	}
	
	public String getAnnotator() {
		return annotator;
	}

	public void setAnnotator(String annotator) {
		this.annotator = annotator;
	}

	public Set<String> getPids() {
		return pids;
	}

	public void setPids(Set<String> pids) {
		this.pids = pids;
	}
	
	public int getQueryID() {
		return queryID;
	}

	public void setQueryID(int queryID) {
		this.queryID = queryID;
	}

	public Set<String> getRelevantPids() {
		return relevantPids;
	}

	public void setRelevantPids(Set<String> relevantPids) {
		this.relevantPids = relevantPids;
	}
}
