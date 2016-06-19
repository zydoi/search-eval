package com.litb.search.eval.dto;

public enum SolrCore {
	
	prod("Production Solr Core"),
	eval("Evaluation Solr Core");
	
	private String desc;
	
	private SolrCore(String desc) {
		this.desc = desc;
	}
	
	public String getDesc() {
		return this.desc;
	}
}
