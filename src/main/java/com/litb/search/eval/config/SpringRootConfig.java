package com.litb.search.eval.config;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.h2.server.web.WebServlet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
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
		return new HttpSolrServer(env.getProperty("solr.prod.url"));
	}

	@Bean(name="SolrEvalServer")
	public SolrServer getSolrEvalServer() {
		return new HttpSolrServer(env.getProperty("solr.eval.url"));
	}
	
	@Bean(name="SolrSuggestServer")
	public SolrServer getSolrSuggestServer() {
		return new HttpSolrServer(env.getProperty("solr.suggest.url"));
	}
	
	@Bean
	public ServletRegistrationBean h2servletRegistration() {
	    ServletRegistrationBean registration = new ServletRegistrationBean(new WebServlet());
	    registration.addUrlMappings("/h2/console/*");
	    registration.addInitParameter("-webAllowOthers", "true");
	    return registration;
	}
}
