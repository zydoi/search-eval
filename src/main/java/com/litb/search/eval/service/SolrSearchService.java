package com.litb.search.eval.service;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class SolrSearchService {

	@Autowired
	@Qualifier("SolrTemplate")
	private RestTemplate restTemplate;
	
	@Autowired
	private Environment environment;
	
	public String query(String id) {
		String solrAPI = environment.getProperty("solr.url") + "/select";
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(solrAPI)
				.queryParam("q", "id:"+id);
		URI uri = builder.build().encode().toUri();
		return restTemplate.getForObject(uri, String.class);
	}
}
