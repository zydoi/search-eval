package com.litb.search.eval.controller;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.litb.search.eval.dto.ItemsResultDTO;
import com.litb.search.eval.dto.SearchResultDTO;
import com.litb.search.eval.dto.SolrCore;
import com.litb.search.eval.dto.SolrItemDTO;
import com.litb.search.eval.service.AnnotateService;
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
	
	@Autowired
	private AnnotateService annotateService;
	
	@RequestMapping(value="testSearch", method=RequestMethod.GET, produces="application/json")
	public SearchResultDTO search(@RequestParam String keywords) {
		SearchResultDTO result = litbService.search(keywords, false);
		return result;
	}
	
	@RequestMapping(value="testItemsGet", method=RequestMethod.GET, produces="application/json")
	public ItemsResultDTO itemsGet(@RequestParam String keywords) {
		return litbService.getItems(keywords, false);
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
	
	@RequestMapping(value="testAnnotate", method=RequestMethod.GET)
	public String annotate(@RequestParam String id) {
		Set<Integer> ids = new HashSet<>();
		ids.add(Integer.valueOf(id));
		annotateService.annotate("me", "dresses", ids);
		return "done";
	}
}
