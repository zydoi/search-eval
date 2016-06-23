package com.litb.search.eval.controller;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.litb.search.eval.dto.AnnotateDTO;
import com.litb.search.eval.dto.ItemsResultDTO;
import com.litb.search.eval.repository.QueryRepository;
import com.litb.search.eval.service.EvaluationService;
import com.litb.search.eval.service.LitbSearchService;

@Controller
public class AnnotateController {

	private static final Logger LOGGER = Logger.getLogger(AnnotateController.class);

	@Autowired
	private QueryRepository keywordService;

	@Autowired
	private LitbSearchService litbService;
	
	@Autowired
	private EvaluationService evaluationService;

	@ModelAttribute("queries")
	public Map<Integer, String> populateQueries() {
		return keywordService.getAllQueries();
	}

	@RequestMapping(value = { "/", "/select" }, method = RequestMethod.GET)
	public String selectQuery(@RequestParam(required = false) String annotator, Model model, HttpSession session) {
		if (annotator != null) {
			session.setAttribute("annotator", annotator);
		}
		return "items";
	}

	@RequestMapping(value = "/items", method = RequestMethod.GET)
	public String selectQuery(@RequestParam String query, @RequestParam(required = false) String annotator, Model model, HttpSession session) {
		LOGGER.info(annotator + " start to annotate items for query: " + query);
		if (annotator != null) {
			session.setAttribute("annotator", annotator);
		}
		
		ItemsResultDTO items = litbService.getItems(query);
		
		model.addAttribute("query", query);
		model.addAttribute("items", items);
		model.addAttribute("name", annotator);
		model.addAttribute("annotateDTO", new AnnotateDTO(annotator, query));
		
		return "items";
	}
	
	@RequestMapping(value = "/annotate", method = RequestMethod.POST)
	public String annotate(AnnotateDTO annotateDTO) {
		LOGGER.info(annotateDTO.getAnnotator() + " finished annotating query: " + annotateDTO.getQuery());

		evaluationService.annotate(annotateDTO.getAnnotator(), annotateDTO.getQuery(), annotateDTO.getPids());

		return "items";
	}
}
