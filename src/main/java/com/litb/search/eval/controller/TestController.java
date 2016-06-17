package com.litb.search.eval.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.litb.search.eval.dto.SearchResultDTO;
import com.litb.search.eval.service.LitbSearchService;
import com.litb.search.eval.service.SolrSearchService;

@RestController
@RequestMapping(value = "/", produces = MediaType.TEXT_PLAIN_VALUE)
public class TestController {

	@Autowired
	private LitbSearchService litbService;
	
	@Autowired
	private SolrSearchService solrService;
	
	@RequestMapping(value="search", method=RequestMethod.GET, produces="application/json")
	public List<String> search(@RequestParam String keywords) {
		SearchResultDTO result = litbService.search(keywords);
		return result.getInfo().getItems();
	}
	
	@RequestMapping(value="solr", method=RequestMethod.GET, produces="application/json")
	public String solrSearch(@RequestParam String id) {
		return solrService.query(id);
	}
}
