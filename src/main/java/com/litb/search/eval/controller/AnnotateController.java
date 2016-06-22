package com.litb.search.eval.controller;

import java.util.Collection;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.litb.search.eval.dto.ItemsResultDTO;
import com.litb.search.eval.service.KeywordService;
import com.litb.search.eval.service.LitbSearchService;
import com.litb.search.eval.service.SolrIndexService;

@Controller
public class AnnotateController {

	@Autowired
	private SolrIndexService indexService;
	
	@Autowired
	private KeywordService keywordService;
	
	@Autowired
	private LitbSearchService litbService;
	
	@ModelAttribute("queries")
	public Collection<String> populateQueries() {
		return keywordService.getAllQueries().values();
	}
	
	@RequestMapping(value="/items", method=RequestMethod.GET)
	public String annotateTest(@RequestParam(required=false, defaultValue="tester") String annotator, String query, Model model) {
        model.addAttribute("name", annotator);
		return "items";
	}
	
	
	@RequestMapping(value="/annotate", method=RequestMethod.POST)
	public String annotate(@RequestParam String queryID, @RequestParam String annotator, @RequestParam Set<String> ids) {
		//TODO write to file.
		
		indexService.annotate(queryID, ids);
		return "";
	}
	
	@RequestMapping(value="/items", params={"selectQuery"}, method=RequestMethod.GET)
	public String selectQuery(@ModelAttribute String query, Model model) {
		ItemsResultDTO items = litbService.getItems(query);

		model.addAttribute("query", query);
		model.addAttribute("items", items);
		
		return "items";
	}
	
}
