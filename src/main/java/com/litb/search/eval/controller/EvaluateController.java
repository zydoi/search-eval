package com.litb.search.eval.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.litb.search.eval.dto.EvalResultDTO;
import com.litb.search.eval.service.EvaluationService;

@Controller
@RequestMapping("eval")
public class EvaluateController {

	@Autowired
	private EvaluationService evalService;
	
	@RequestMapping
	public ModelAndView evaluateAll(ModelAndView modelAndView) {
		modelAndView.setViewName("statistic");
		
		EvalResultDTO result = evalService.generateEvaluationResult();
		
		modelAndView.addObject("queryResults", result.getQueryEvalResults());
		modelAndView.addObject("map", result.getMap());
		
		return modelAndView;
	}
}
