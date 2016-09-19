package com.litb.search.eval.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.litb.search.eval.dto.litb.ItemsResultDTO;
import com.litb.search.eval.service.LitbSearchService;

@SessionAttributes("solr")
@Controller
public class SearchController {

	@Autowired
	private LitbSearchService litbService;

	@ModelAttribute("page")
	public String module() {
		return "search";
	}

	@RequestMapping("/search")
	public String search(@RequestParam String solr,  Model model) {
		model.addAttribute("solr", solr);
		return "search";
	}

	@RequestMapping(value = "/search", params = { "query" })
	public String search(HttpServletRequest req, Model model) {
		String query = req.getParameter("query");
		ItemsResultDTO items;
		if (req.getSession().getAttribute("solr") == null) {
			model.addAttribute("solr", "online");
		}
		if (req.getSession().getAttribute("solr").equals("eval")) {
			items = litbService.getItems(query, true);
		} else {
			items = litbService.getItems(query, false);
		}
		model.addAttribute("query", query);
		model.addAttribute("items", items);
		return "search";
	}
}
