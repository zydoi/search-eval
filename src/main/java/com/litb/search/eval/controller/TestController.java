package com.litb.search.eval.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.litb.search.eval.dto.SearchResultDTO;
import com.litb.search.eval.dto.SolrCore;
import com.litb.search.eval.dto.SolrItemDTO;
import com.litb.search.eval.service.LitbSearchService;
import com.litb.search.eval.service.SolrIndexService;
import com.litb.search.eval.service.SolrSearchService;

@RestController
@RequestMapping(value = "/", produces = MediaType.TEXT_PLAIN_VALUE)
public class TestController {

	@Autowired
	private LitbSearchService litbService;
	
	@Autowired
	private SolrSearchService searchService;
	
	@Autowired
	private SolrIndexService indexService;
	
	@RequestMapping(value="testSearch", method=RequestMethod.GET, produces="application/json")
	public List<String> search(@RequestParam String keywords) {
		SearchResultDTO result = litbService.search(keywords);
		return result.getInfo().getItems();
	}
	
	@RequestMapping(value="solr", method=RequestMethod.GET, produces="application/json")
	public String solrSearch(@RequestParam String id) {
		return searchService.getItem(id).getName();
	}

	@RequestMapping(value="testIndex", method=RequestMethod.GET, produces="application/json")
	public String index(@RequestParam String id) {
		SolrItemDTO item = searchService.getItem(id);
		indexService.addItem(item);
		return searchService.query(id, SolrCore.EVAL);
	}
	
}
