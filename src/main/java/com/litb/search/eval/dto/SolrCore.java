package com.litb.search.eval.dto;

public enum SolrCore {
	
	PROD("Production Solr Core"),
	EVAL("Evaluation Solr Core");
	
	private String desc;
	
	private SolrCore(String desc) {
		this.desc = desc;
	}
	
	public String getDesc() {
		return this.desc;
	}
}
