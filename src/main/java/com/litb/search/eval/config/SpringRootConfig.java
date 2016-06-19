package com.litb.search.eval.config;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.client.RestTemplate;

@Configuration
public class SpringRootConfig {
	
	@Autowired
	private Environment env;
	
	@Bean(name="LitbTemplate")
	public RestTemplate getLitbTemplate() {
		return new RestTemplate();
	}
	
	@Bean(name="SolrTemplate")
	public RestTemplate getSolrTemplate() {
		return new RestTemplate();
	}
	
	@Bean(name="SolrProdServer")
	public SolrServer getSolrProdServer() {
		SolrServer solrServer = new HttpSolrServer(env.getProperty("solr.prod.url"));
		return solrServer;
	}

	@Bean(name="SolrEvalServer")
	public SolrServer getSolrEvalServer() {
		SolrServer solrServer = new HttpSolrServer(env.getProperty("solr.eval.url"));
		return solrServer;
	}
}
