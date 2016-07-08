package com.litb.search.eval.controller;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.litb.search.eval.dto.AnnotateDTO;
import com.litb.search.eval.dto.litb.ItemDTO;
import com.litb.search.eval.dto.litb.ItemsResultDTO;
import com.litb.search.eval.entity.EvalItem;
import com.litb.search.eval.entity.EvalQuery;
import com.litb.search.eval.repository.AnnotationRepository;
import com.litb.search.eval.repository.ItemRepository;
import com.litb.search.eval.repository.QueryRepository;
import com.litb.search.eval.service.AnnotateService;
import com.litb.search.eval.service.ItemService;
import com.litb.search.eval.service.LitbSearchService;
import com.litb.search.eval.service.util.SolrQueryUtils;

@SessionAttributes("items")
@Controller
public class AnnotateController {

	private static final Logger LOGGER = LoggerFactory.getLogger(AnnotateController.class);

	@Autowired
	private QueryRepository queryRepo;

	@Autowired
	private LitbSearchService litbService;
	
	@Autowired
	private AnnotateService annotateService;
	
	@Autowired
	private ItemRepository itemRepo;
	
	@Autowired
	private AnnotationRepository annotationRepo;
	
	@Autowired
	private ItemService itemService;

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
	public String selectQuery(@RequestParam int queryID, @RequestParam(defaultValue="false") boolean onlyNew, @RequestParam(required = false) String annotator, 
			@RequestParam(defaultValue="false") boolean isOnline, Model model, HttpSession session) {
		if (annotator != null) {
			session.setAttribute("annotator", annotator);
		}
		EvalQuery query = queryRepo.findOne(queryID);
		LOGGER.info(session.getAttribute("annotator") + " start to annotate items for query: " + query);
		ItemsResultDTO items = litbService.getItems(query.getName(), !isOnline);
		
		List<String> ids = items.getInfo().getProductsList();
		
		if(!isOnline) { // enrich item details
			items.getInfo().getItems().clear();
			Set<String> relevantIds = annotationRepo.findRelevantItemIds(queryID);
			for (String id : ids) {
				if (onlyNew && annotationRepo.findByQueryIdAndItemId(queryID, id) != null) {
					continue;
				}
				
				EvalItem item = itemRepo.findOne(id);
				if(item == null) {
					LOGGER.error("Item {} does not exist!", id);
					continue;
				}
				ItemDTO dto = new ItemDTO(item);
				if (relevantIds.contains(id)) {
					dto.setRelevant(true);
				}
				

				items.getInfo().getItems().add(dto);
			}
		} else {
			// mark new items
			Set<String> nonExsitIDs = itemService.getNonExistIds(ids);
			Set<String> relevantIds = annotationRepo.findRelevantItemIds(queryID);

			if (!nonExsitIDs.isEmpty()) {
				Iterator<ItemDTO> iter = items.getInfo().getItems().iterator();
				while (iter.hasNext()) {
					ItemDTO item = iter.next();
					
					if (relevantIds.contains(item.getItemId())) {
						item.setRelevant(true);
					}
					
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
		}
		// mark relevant items
		model.addAttribute("query", query.getName());
		model.addAttribute("items", items.getInfo().getItems());
		model.addAttribute("name", annotator);
		model.addAttribute("annotateDTO", new AnnotateDTO(annotator, queryID, ids));
		
		return "items";
	}
	
	@RequestMapping(value = "/annotate", method = RequestMethod.POST)
	public String annotate(AnnotateDTO annotateDTO, @ModelAttribute("items") List<ItemDTO> items) {
		annotateService.annotate(annotateDTO.getAnnotator(), annotateDTO.getQueryID(), annotateDTO.getPids(), annotateDTO.getRelevantPids(), items);
		LOGGER.info(annotateDTO.getAnnotator() + " finished annotating query " +  annotateDTO.getQueryID());
		return "items";
	}
}
