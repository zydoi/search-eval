package com.litb.search.eval.dto;

import java.util.Map;
import java.util.TreeMap;

public class QueryEvalResultDTO {
	
	private String queryID;
	
	private String queryName;
	
	private double ap;

	private Map<Integer, Double> precisions = new TreeMap<>();
	
	public QueryEvalResultDTO(String queryID) {
		this.queryID = queryID;
	}
	
	public Map<Integer, Double> getPrecisions() {
		return precisions;
	}

	public void addPrecision(int n, double precision) {
		this.precisions.put(n, precision);
	}

	public String getQueryName() {
		return queryName;
	}

	public void setQueryName(String queryName) {
		this.queryName = queryName;
	}

	public double getAp() {
		return ap;
	}

	public void setAp(double ap) {
		this.ap = ap;
	}

	public String getQueryID() {
		return queryID;
	}

	public void setQueryID(String queryID) {
		this.queryID = queryID;
	}
}
