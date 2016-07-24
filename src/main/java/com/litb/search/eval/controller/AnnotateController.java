package com.litb.search.eval.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

@SessionAttributes({"items", "annotator"})
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
	
	@Value("${annotate.size}")
	private int querySize;

	@ModelAttribute("queries")
	public List<EvalQuery> populateQueries() {
		return queryRepo.findByEffectiveTrue();
	}

    @ModelAttribute("page")
    public String module() {
        return "anno";
    }
	
	@RequestMapping(value ="/select", method = RequestMethod.GET)
	public String selectQuery() {
		return "items";
	}

	@RequestMapping(value = "/itemlist", method = RequestMethod.GET)
	public String selectQuery(@RequestParam int queryID, @RequestParam(defaultValue="false") boolean onlyNew, @RequestParam(required = false) String annotator, 
			@RequestParam(defaultValue="false") boolean isOnline, Model model, HttpSession session) {
		EvalQuery query = queryRepo.findOne(queryID);
		LOGGER.info(session.getAttribute("annotator") + " starts to annotate items for query: " + query);
		
		
		List<ItemDTO> items;
		List<String> ids;
		if(!isOnline) { // annotate evaluation library
			ids = litbService.search(query.getName(), querySize, !isOnline).getInfo().getItems();
			Set<String> relevantIds = annotationRepo.findRelevantItemIds(queryID);
			items = new ArrayList<>();
			for (String id : ids) {
				if (onlyNew && annotationRepo.findByQueryIdAndItemId(queryID, id) != null) {
					continue;
				}
				
				EvalItem item = itemRepo.findOne(id);
				if (item == null) {
					LOGGER.error("Item {} does not exist!", id);
					continue;
				}
				ItemDTO dto = new ItemDTO(item);
				if (relevantIds.contains(id)) {
					dto.setRelevant(true);
				}
				
				items.add(dto);
			}
		} else { // annotate online items
			ItemsResultDTO resultDto = litbService.getItems(query.getName(), !isOnline);
			items = resultDto.getInfo().getItems();
			ids = resultDto.getInfo().getProductsList();

			Set<String> nonExsitIDs = itemService.getNonExistIds(ids);
			Set<String> relevantIds = annotationRepo.findRelevantItemIds(queryID);

			if (!nonExsitIDs.isEmpty()) {
				Iterator<ItemDTO> iter = resultDto.getInfo().getItems().iterator();
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
		if (annotator != null) {
			model.addAttribute("annotator", annotator);
		}
		model.addAttribute("query", query.getName());
		model.addAttribute("items", items);
		model.addAttribute("annotateDTO", new AnnotateDTO(annotator, queryID, ids));
		
		return "items";
	}
	
	@RequestMapping(value = "/annotate", method = RequestMethod.POST)
	public String annotate(AnnotateDTO annotateDTO, @ModelAttribute("items") List<ItemDTO> items) {
		annotateService.annotate(annotateDTO.getAnnotator(), annotateDTO.getQueryID(), annotateDTO.getPids(), annotateDTO.getRelevantPids(), annotateDTO.getIrrelevantPids(), items);
		LOGGER.info(annotateDTO.getAnnotator() + " finished annotating query " +  annotateDTO.getQueryID());
		return "items";
	}
	
}
