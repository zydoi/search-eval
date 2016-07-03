package com.litb.search.eval.controller;

import java.util.Iterator;
import java.util.List;

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
import com.litb.search.eval.dto.litb.ItemDTO;
import com.litb.search.eval.dto.litb.ItemsResultDTO;
import com.litb.search.eval.entity.EvalQuery;
import com.litb.search.eval.repository.QueryRepository;
import com.litb.search.eval.service.AnnotateService;
import com.litb.search.eval.service.LitbSearchService;
import com.litb.search.eval.service.SolrEvalService;
import com.litb.search.eval.service.util.SolrQueryUtils;

@Controller
public class AnnotateController {

	private static final Logger LOGGER = Logger.getLogger(AnnotateController.class);

	@Autowired
	private QueryRepository queryRepo;

	@Autowired
	private LitbSearchService litbService;
	
	@Autowired
	private AnnotateService annotateService;
	
	@Autowired
	private SolrEvalService evalService;

	@ModelAttribute("queries")
	public List<EvalQuery> populateQueries() {
		return queryRepo.findByEffectiveTrue();
	}

	@RequestMapping(value = { "/", "/select" }, method = RequestMethod.GET)
	public String selectQuery(@RequestParam(required = false) String annotator, HttpSession session) {
		if (annotator != null) {
			session.setAttribute("annotator", annotator);
		}
		return "items";
	}

	@RequestMapping(value = "/itemlist", method = RequestMethod.GET)
	public String selectQuery(@RequestParam int queryID, @RequestParam(defaultValue="false") boolean onlyNew, @RequestParam(required = false) String annotator, Model model, HttpSession session) {
		if (annotator != null) {
			session.setAttribute("annotator", annotator);
		}
		EvalQuery query = queryRepo.findOne(queryID);
		LOGGER.info(session.getAttribute("annotator") + " start to annotate items for query: " + query);
		ItemsResultDTO items = litbService.getItems(query.getName());
		
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
			if (!nonExsitIDs.isEmpty()) {
				LOGGER.info("New items for query {" + query + "}: " + SolrQueryUtils.concatIDs(nonExsitIDs));
			}
		}

		model.addAttribute("query", query.getName());
		model.addAttribute("items", items);
		model.addAttribute("name", annotator);
		model.addAttribute("annotateDTO", new AnnotateDTO(annotator, queryID, ids));
		
		return "items";
	}
	
	@RequestMapping(value = "/annotate", method = RequestMethod.POST)
	public String annotate(AnnotateDTO annotateDTO) {
		annotateService.annotate(annotateDTO.getAnnotator(), annotateDTO.getQueryID(), annotateDTO.getPids(), annotateDTO.getRelevantPids());
		LOGGER.info(annotateDTO.getAnnotator() + " finished annotating query " +  annotateDTO.getQueryID());

		return "items";
	}
}
