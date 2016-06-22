package com.litb.search.eval.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import com.litb.search.eval.dto.LitbInfoDTO;
import com.litb.search.eval.service.KeywordService;
import com.litb.search.eval.service.LitbSearchService;
import com.litb.search.eval.service.SolrIndexService;

@Controller
public class AnnotateController {

	private static final Logger LOGGER = Logger.getLogger(AnnotateController.class);
	
	@Autowired
	private SolrIndexService indexService;
	
	@Autowired
	private KeywordService keywordService;
	
	@Autowired
	private LitbSearchService litbService;
	
	@ModelAttribute("queries")
	public Map<Integer, String> populateQueries() {
		return keywordService.getAllQueries();
	}
	
	@RequestMapping(value={"/","/select"}, method=RequestMethod.GET)
	public String annotateTest(@RequestParam(required=false, defaultValue="tester") String annotator, Model model) {
        model.addAttribute("name", annotator);
		return "items";
	}
	
	
	@RequestMapping(value="/annotate", method=RequestMethod.POST)
	public String annotate(AnnotateDTO annotateDTO) {
		
		indexService.annotate(annotateDTO.getAnnotator(), annotateDTO.getQuery(), annotateDTO.getPids());
		
		return "items";
	}
	
	@RequestMapping(value="/items", method=RequestMethod.GET)
	public String selectQuery(@RequestParam String query, @RequestParam(required=false, defaultValue="tester") String annotator, Model model) {
		LOGGER.info("Start annotate items for query: " + query);
//		ItemsResultDTO items = litbService.getItems(query);

		ItemsResultDTO items = new ItemsResultDTO();
		items.setResult("success");
		LitbInfoDTO infoDTO = new LitbInfoDTO();
		List<ItemDTO> is = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			ItemDTO item = new ItemDTO();
			item.setCurrency("USD");
			item.setFavoriteTimes(i);
			item.setItemName("test item");
			item.setSalePrice(i);
			item.setReviewCount(100/(i+1));
			item.setItemId(String.valueOf(i));
			item.setMainImgURL("http://litbimg8.rightinthebox.com/images/182x240/201508/russtt1439971810458.jpg");
			is.add(item);
		}
		infoDTO.setItems(is);
		items.setInfo(infoDTO);
		
		model.addAttribute("query", query);
		model.addAttribute("items", items);
        model.addAttribute("name", annotator);
        model.addAttribute("annotateDTO", new AnnotateDTO());
		
		return "items";
	}
	
}
