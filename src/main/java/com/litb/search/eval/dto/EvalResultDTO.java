package com.litb.search.eval.dto;

import java.util.Map;
import java.util.TreeMap;

public class EvalResultDTO {

	private String title;
	
	private double map;
	
	private Map<Integer, Double> averagePn;
	
	private Map<Integer, QueryEvalResultDTO> queryEvalResults = new TreeMap<>();

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Map<Integer, QueryEvalResultDTO> getQueryEvalResults() {
		return queryEvalResults;
	}

	public double getMap() {
		return map;
	}
	
	public void setMap(double map) {
		this.map = map;
	}

	public void addQueryResult(QueryEvalResultDTO queryResult) {
		this.queryEvalResults.put(Integer.valueOf(queryResult.getQueryID()), queryResult);
	}
	
	public Map<Integer, Double> getAveragePn() {
		return averagePn;
	}

	public void setAveragePn(Map<Integer, Double> averagePn) {
		this.averagePn = averagePn;
	}
}
