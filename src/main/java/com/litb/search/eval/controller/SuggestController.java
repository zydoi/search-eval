package com.litb.search.eval.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController("/suggest")
public class SuggestController {

	private static final Logger LOGGER = LoggerFactory.getLogger(SuggestController.class);
	
	@RequestMapping(value = "suggest", method = RequestMethod.GET, produces = "application/json")
	public String getSuggestion(@RequestParam String query) {
		LOGGER.info("Suggestion: {}", query);
		return query;
	}
}
