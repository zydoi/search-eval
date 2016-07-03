package com.litb.search.eval.service;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.litb.search.eval.dto.litb.ItemsResultDTO;
import com.litb.search.eval.dto.litb.SearchResultDTO;

@Service
public class LitbSearchService {
	
	@Autowired
	@Qualifier("LitbTemplate")
	private RestTemplate restTemplate;

	@Autowired
	private Environment environment;

	public SearchResultDTO search(String keywords, int size) {
		return search(keywords, size, false);
	}
	
	public ItemsResultDTO getItems(String keywords) {
		return getItems(keywords, false);
	}
	
	public SearchResultDTO search(String keywords, int size, boolean isEval) {
		String url = isEval ? environment.getProperty("litb.eval.api") : environment.getProperty("litb.eval.api");
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url + "keywordSearch")
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
				.queryParam("page_size", size)
				.queryParam("query", keywords);

		URI uri = builder.build().encode().toUri();

		return restTemplate.getForObject(uri, SearchResultDTO.class);
	}

	public ItemsResultDTO getItems(String keywords, boolean isEval) {
		String url = isEval ? environment.getProperty("litb.vela.eval.api") : environment.getProperty("litb.vela.api");
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
				.queryParam("client", "vela")
				.queryParam("format", "json")
				.queryParam("method", "vela.items.get")
				.queryParam("app_key", "V06GF3A2")
				.queryParam("app_secret", "f838905ddd031399ffdm8n3mymrrzomk")
				.queryParam("v", "1.1")
				.queryParam("language", "en")
				.queryParam("currency", "USD")
				.queryParam("sid", "eval_123")
				.queryParam("sort_by", "2d")
				.queryParam("page_no", "1")
				.queryParam("page_size", environment.getProperty("search.size"))
				.queryParam("is_hd", "false")
				.queryParam("country", "USA")
				.queryParam("timestamp", "2016-06-12+00:00:00")
				.queryParam("sign_method", "md5")
				.queryParam("sign", "123")
				.queryParam("query", keywords);
		
		URI uri = builder.build().encode().toUri();
		HttpHeaders headers = new HttpHeaders();
		headers.add("Cookie", "hack=123");
		HttpEntity<?> requestEntity = new HttpEntity<>(null, headers);
		return restTemplate.exchange(uri,  HttpMethod.GET,requestEntity, ItemsResultDTO.class).getBody();
	}
}
