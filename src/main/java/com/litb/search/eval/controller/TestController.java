package com.litb.search.eval.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.litb.search.eval.dto.SolrCore;
import com.litb.search.eval.dto.litb.ItemsResultDTO;
import com.litb.search.eval.dto.litb.SearchResultDTO;
import com.litb.search.eval.dto.solr.SolrItemDTO;
import com.litb.search.eval.service.LitbSearchService;
import com.litb.search.eval.service.SolrEvalService;
import com.litb.search.eval.service.SolrProdService;

@RestController
@RequestMapping(value = "/")
public class TestController {

	@Autowired
	private LitbSearchService litbService;
	
	@Autowired
	private SolrProdService searchService;
	
	@Autowired
	private SolrEvalService indexService;
	
	@RequestMapping(value="testSearch", method=RequestMethod.GET, produces="application/json")
	public SearchResultDTO search(@RequestParam String keywords) {
		SearchResultDTO result = litbService.search(keywords, 100);
		return result;
	}
	
	@RequestMapping(value="testItemsGet", method=RequestMethod.GET, produces="application/json")
	public ItemsResultDTO itemsGet(@RequestParam String keywords) {
		return litbService.getItems(keywords);
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
