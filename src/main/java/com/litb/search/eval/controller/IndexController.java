package com.litb.search.eval.controller;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
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
public class IndexController {
	
	private static final Logger LOGGER = Logger.getLogger(IndexController.class);

	@Autowired
	private LitbSearchService litbService;

	@Autowired
	private SolrSearchService searchService;

	@Autowired
	private SolrIndexService indexService;

	@RequestMapping(value = "indexQuery", method = RequestMethod.GET, produces = "application/json")
	public String indexQuery(@RequestParam String query) {
		 SearchResultDTO result = litbService.search(query);
		 List<String> ids = result.getInfo().getItems();
		 LOGGER.info("Start to index products: " +  StringUtils.collectionToCommaDelimitedString(ids));
		 List<SolrItemDTO> items = searchService.getItems(ids);
		 indexService.addItems(items);

		return searchService.query(ids, SolrCore.EVAL);
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
		
		return searchService.query(id, SolrCore.EVAL);
	}
	
	@RequestMapping(value = "delete", method = RequestMethod.GET, produces = "application/json")
	public void delete(@RequestParam String id) {
		indexService.deleteItem(id);
	}
}
