package com.litb.search.eval.repository;

public enum QueryType {
	TOP("top_queries.txt"), BAD("bad_queries.txt"), MISSPELL("mispell_queriex.txt"), SYNM("synm_queries.txt"), ALL("");

	String fileName;

	QueryType(String fileName) {
		this.fileName = fileName;
	}
	
	public String getFileName() {
		return this.fileName;
	}
}
