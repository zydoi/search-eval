package com.litb.search.eval.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.litb.search.eval.dto.QueryDTO;
import com.litb.search.eval.entity.EvalQuery;
import com.litb.search.eval.repository.QueryType;
import com.litb.search.eval.service.QueryService;

@Controller
@RequestMapping("query")
public class QueryController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(QueryController.class);
	
	@Autowired
	private QueryService queryService;
	
	@RequestMapping(value = "/list",  method = RequestMethod.GET)
	public String listAllQueries(@RequestParam(defaultValue="ALL") QueryType queryType, Model model) {
		List<EvalQuery> queries = queryService.findQueriesByType(queryType);
		model.addAttribute("queries", queries);
		return "queries";
	}
	
	@RequestMapping(value = "/add",  method = RequestMethod.GET)
	public String addQuery(QueryDTO query) {
		LOGGER.info("Added Query {}", query);
		return null;
	}
	
	@RequestMapping(value = "/delete",  method = RequestMethod.GET)
	public String deleteQuery(int queryID) {
		LOGGER.info("Deleted Query {}", queryID);
		return "queries";
	}
}
