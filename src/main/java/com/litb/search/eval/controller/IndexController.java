package com.litb.search.eval.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.litb.search.eval.dto.SolrCore;
import com.litb.search.eval.dto.SolrItemDTO;
import com.litb.search.eval.service.LitbSearchService;
import com.litb.search.eval.service.SolrIndexService;
import com.litb.search.eval.service.SolrSearchService;

@RestController
public class IndexController {

	@Autowired
	private LitbSearchService litbService;

	@Autowired
	private SolrSearchService searchService;

	@Autowired
	private SolrIndexService indexService;

	@RequestMapping(value = "indexQuery", method = RequestMethod.GET, produces = "application/json")
	public String indexQuery(@RequestParam String query) {
		// SearchResultDTO id = litbService.search(query);
		// String response = solrService.query(query, SolrCore.prod);

		return null;
	}

	@RequestMapping(value = "indexAll", method = RequestMethod.GET, produces = "application/json")
	public String index() {
		return null;
	}

	@RequestMapping(value = "index", method = RequestMethod.GET, produces = "application/json")
	public String index(@RequestParam String id) {
		SolrItemDTO item = searchService.getItem(id);
		if (item != null) {
			indexService.addItem(item);
		}
		
		return searchService.query(id, SolrCore.eval);
	}
}
