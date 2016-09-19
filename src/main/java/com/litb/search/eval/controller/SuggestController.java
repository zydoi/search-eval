package com.litb.search.eval.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.litb.search.eval.service.SolrSuggestService;

@RestController
public class SuggestController {

	private static final Logger LOGGER = LoggerFactory.getLogger(SuggestController.class);
	
	@Autowired
	private SolrSuggestService suggService;
	
	@RequestMapping(value = "suggest", method = RequestMethod.GET, produces = "application/json")
	public List<String> getSuggestion(@RequestParam String query, String lang) {
		LOGGER.info("Suggestion: {}", query);
		return suggService.suggest(query, lang);
	}
}
