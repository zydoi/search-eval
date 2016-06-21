package com.litb.search.eval.controller;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.litb.search.eval.service.SolrIndexService;

@Controller
public class AnnotateController {

	@Autowired
	private SolrIndexService indexService;
	
	@RequestMapping(value="/helloworld", method=RequestMethod.GET)
	public String helloworld(@RequestParam(name="name", required=false, defaultValue="World") String name, Model model) {
        model.addAttribute("name", name);
		return "greeting";
	}
	
	@RequestMapping(value="/annotate", method=RequestMethod.POST)
	public void annotate(@RequestParam String queryId, @RequestParam String annotator, @RequestParam Set<String> ids) {
		//TODO write to file.
		
		indexService.annotate(queryId, ids);
	}
	
}
