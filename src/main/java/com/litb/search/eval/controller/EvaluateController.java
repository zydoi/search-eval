package com.litb.search.eval.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.litb.search.eval.dto.EvalResultDTO;
import com.litb.search.eval.repository.QueryType;
import com.litb.search.eval.service.EvaluationService;
import com.litb.search.eval.service.SolrEvalConfigService;

@Controller
@RequestMapping("eval")
public class EvaluateController {

	@Autowired
	private EvaluationService evalService;
	
	@Autowired
	private SolrEvalConfigService solrConfigService;
	
    @ModelAttribute("page")
    public String module() {
        return "eval";
    }
	
	@RequestMapping
	public ModelAndView evaluateAll(@RequestParam(required=false) QueryType queryType, ModelAndView modelAndView) {
		modelAndView.setViewName("eval");
		
		Properties props = solrConfigService.loadSolrProps();
		modelAndView.addObject("props", props);

		List<QueryType> types = new ArrayList<>();
		types.add(QueryType.ALL);
		types.add(QueryType.TOP);
		types.add(QueryType.BAD);
		types.add(QueryType.MUL);
		modelAndView.addObject("types", types);

		if (queryType != null) {
			EvalResultDTO result = evalService.generateEvaluationResult(queryType);
			
			
			modelAndView.addObject("queryResults", result.getQueryEvalResults());
			modelAndView.addObject("map", result.getMap());
			modelAndView.addObject("queryType", queryType);
			modelAndView.addObject("p10", result.getAveragePn().get(10));
			modelAndView.addObject("p20", result.getAveragePn().get(20));
			modelAndView.addObject("p48", result.getAveragePn().get(48));
		}
		return modelAndView;
	}
	
	@RequestMapping("/update")
	public ModelAndView updateSolrProperties(@RequestParam Map<String, String> properties, ModelAndView modelAndView) {
		modelAndView.setViewName("eval");
		boolean result = solrConfigService.updateSolrProps(properties);
		
		return modelAndView;
	}

}