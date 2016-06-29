package com.litb.search.eval.repository;

public enum QueryType {
	
	TOP("top_queries.txt"), BAD("bad_queries.txt"), MISSPELL("misspell_queries.txt");

	private String fileName;

	QueryType(String fileName) {
		this.fileName = fileName;
	}

	public String getFileName() {
		return this.fileName;
	}

}
