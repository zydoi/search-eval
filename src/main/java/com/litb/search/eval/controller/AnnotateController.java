package com.litb.search.eval.controller;

import java.util.Iterator;
import java.util.List;
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
import com.litb.search.eval.dto.ItemDTO;
import com.litb.search.eval.dto.ItemsResultDTO;
import com.litb.search.eval.repository.QueryRepository;
import com.litb.search.eval.service.AnnotateService;
import com.litb.search.eval.service.LitbSearchService;
import com.litb.search.eval.service.SolrEvalService;
import com.litb.search.eval.service.util.SolrQueryUtils;

@Controller
public class AnnotateController {

	private static final Logger LOGGER = Logger.getLogger(AnnotateController.class);

	@Autowired
	private QueryRepository keywordService;

	@Autowired
	private LitbSearchService litbService;
	
	@Autowired
	private AnnotateService annotateService;
	
	@Autowired
	private SolrEvalService evalService;

	@ModelAttribute("queries")
	public Map<Integer, String> populateQueries() {
		return keywordService.getAllQueries();
	}

	@RequestMapping(value = { "/", "/select" }, method = RequestMethod.GET)
	public String selectQuery(@RequestParam(required = false) String annotator, HttpSession session) {
		if (annotator != null) {
			session.setAttribute("annotator", annotator);
		}
		return "items";
	}

	@RequestMapping(value = "/itemlist", method = RequestMethod.GET)
	public String selectQuery(@RequestParam String queryID, @RequestParam(defaultValue="false") boolean onlyNew, @RequestParam(required = false) String annotator, Model model, HttpSession session) {
		if (annotator != null) {
			session.setAttribute("annotator", annotator);
		}
		LOGGER.info(session.getAttribute("annotator") + " start to annotate items for query: " + keywordService.getQueryByID(queryID));
		String query = keywordService.getQueryByID(queryID);
		ItemsResultDTO items = litbService.getItems(query, true);
		
		List<String> ids = items.getInfo().getProductsList();
		List<String> nonExsitIDs = evalService.getNonExsitIDs(ids);
		
		if (!nonExsitIDs.isEmpty()) {
			Iterator<ItemDTO> iter = items.getInfo().getItems().iterator();
			while (iter.hasNext()) {
				ItemDTO item = iter.next();
				if(nonExsitIDs.contains(item.getItemId())) {
					item.setNew(true);
				} else if (onlyNew) {
					iter.remove();
				}
			}
			LOGGER.info("New items for query {" + query + "}: " + SolrQueryUtils.concatIDs(nonExsitIDs));
		}

		model.addAttribute("query", query);
		model.addAttribute("items", items);
		model.addAttribute("name", annotator);
		model.addAttribute("annotateDTO", new AnnotateDTO(annotator, queryID));
		
		return "items";
	}
	
	@RequestMapping(value = "/annotate", method = RequestMethod.POST)
	public String annotate(AnnotateDTO annotateDTO) {
		LOGGER.info(annotateDTO.getAnnotator() + " finished annotating query: " + keywordService.getQueryByID(annotateDTO.getQueryID()));

		annotateService.annotate(annotateDTO.getAnnotator(), annotateDTO.getQueryID(), annotateDTO.getPids());

		return "items";
	}
}
