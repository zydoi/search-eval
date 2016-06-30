package com.litb.search.eval.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.litb.search.eval.dto.ItemsResultDTO;
import com.litb.search.eval.service.LitbSearchService;

@Controller
public class SearchController {
	
	@Autowired
	private LitbSearchService litbService;
	
	@RequestMapping("/search")
	public String search() {
		return "results";
	}
	
	@RequestMapping(value="/search", params={"query"})
	public String search(HttpServletRequest req, Model model) {
		String query = req.getParameter("query");
		ItemsResultDTO items = litbService.getItems(query, false);
		
		model.addAttribute("query", query);
		model.addAttribute("items", items);
		return "results";
	}
}
