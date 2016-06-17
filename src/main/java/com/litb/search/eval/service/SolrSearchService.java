package com.litb.search.eval.service;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.litb.search.eval.dto.SearchResultDTO;

@Service
public class SolrSearchService {

	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private Environment environment;

	public SearchResultDTO search(String keywords) {
		
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(environment.getProperty("litb.api"))
				.queryParam("app_key", "FIQ79MXR")
				.queryParam("app_secret", "606caa95b19bc709syqx9cpl72kmmnzy")
				.queryParam("cid", "0")
				.queryParam("client", "vela_pc")
				.queryParam("country", "US")
				.queryParam("currency", "USD")
				.queryParam("cv", "ZUES_CV")
				.queryParam("is_contain_rating", "1")
				.queryParam("language", "en")
				.queryParam("page_no", "1")
				.queryParam("sid", "eval_123")
				.queryParam("sort_by", "2d")
				.queryParam("page_size", environment.getProperty("search.size"))
				.queryParam("query", keywords);
		
		URI uri = builder.build().encode().toUri();
		
		return restTemplate.getForObject(uri, SearchResultDTO.class);
	}
}
