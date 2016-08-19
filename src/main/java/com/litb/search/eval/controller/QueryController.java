package com.litb.search.eval.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.litb.search.eval.dto.QueryDTO;
import com.litb.search.eval.entity.EvalQuery;
import com.litb.search.eval.repository.QueryType;
import com.litb.search.eval.service.QueryService;

@SessionAttributes("queryType")
@Controller
@RequestMapping("query")
public class QueryController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(QueryController.class);
	
	@Autowired
	private QueryService queryService;
	
	@ModelAttribute("types")
	public QueryType[] getQueryTypes() {
		return QueryType.values();
	}
	
    @ModelAttribute("page")
    public String module() {
        return "manage";
    }
	
	@RequestMapping(value = "/list",  method = RequestMethod.GET)
	public String listAllQueries(@RequestParam(defaultValue = "ALL") QueryType queryType, Model model) {
		if (queryType != null) {
			List<EvalQuery> queries = queryService.findQueriesByType(queryType);
			model.addAttribute("queries", queries);
			model.addAttribute("queryType", queryType);
		} 
		return "queries";
	}
	
	@RequestMapping(value = "/create",  method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public QueryDTO createQuery(QueryDTO queryDTO) {
		EvalQuery query = queryService.createQuery(queryDTO);
		if (query != null) {
			LOGGER.info("Added Query: {}", queryDTO);
		} 
		return queryDTO;
	}
	
	@RequestMapping(value = "/delete",  method = RequestMethod.GET)
	public String deleteQuery(int queryId) {
		LOGGER.info("Deleted Query {}", queryId);
		return "queries";
	}
	
	@RequestMapping(value = "/update", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public QueryDTO editQuery(QueryDTO queryDTO) {
		queryService.updateQuery(queryDTO);
		return queryDTO;
	}
	
	@RequestMapping(value = "/popupEdit", method = RequestMethod.GET)
	public String popupEditPage(int queryId, Model model) {
		EvalQuery query = queryService.findById(queryId);
		model.addAttribute("query", query);
		model.addAttribute("operation", "edit");
		model.addAttribute("queryDTO", new QueryDTO(query));
		return "editQuery";
	}
	
	@RequestMapping(value = "/popupCreate", method = RequestMethod.GET)
	public String popupAddPage(Model model) {
		QueryDTO dto = new QueryDTO();
		dto.addType((QueryType) model.asMap().get("queryType"));
		model.addAttribute("queryDTO", dto);
		model.addAttribute("operation", "add");
		return "editQuery";
	}
}
