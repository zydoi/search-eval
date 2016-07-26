package com.litb.search.eval.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.solr.client.solrj.response.UpdateResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.litb.search.eval.dto.SolrCore;
import com.litb.search.eval.dto.litb.ItemDTO;
import com.litb.search.eval.dto.litb.ItemsResultDTO;
import com.litb.search.eval.dto.litb.SearchResultDTO;
import com.litb.search.eval.dto.solr.SolrItemDTO;
import com.litb.search.eval.entity.EvalItem;
import com.litb.search.eval.entity.EvalQuery;
import com.litb.search.eval.repository.QueryRepository;
import com.litb.search.eval.service.ItemService;
import com.litb.search.eval.service.LitbSearchService;
import com.litb.search.eval.service.QueryService;
import com.litb.search.eval.service.SolrEvalService;
import com.litb.search.eval.service.SolrProdService;

@RestController("/index")
public class IndexController {

	private static final Logger LOGGER = LoggerFactory.getLogger(IndexController.class);

	@Autowired
	private LitbSearchService litbService;

	@Autowired
	private SolrProdService searchService;

	@Autowired
	private QueryRepository queryRepo;
	
	@Autowired
	private QueryService queryService;
	
	@Autowired
	private SolrEvalService indexService;
	
	@Autowired
	private ItemService itemService;
	
    @ModelAttribute("page")
    public String module() {
        return "index";
    }
	
	@RequestMapping(value = "indexQuery", method = RequestMethod.GET, produces = "application/json")
	public String indexQuery(@RequestParam int queryId, @RequestParam(defaultValue="true") boolean onlyNew) {
		ItemsResultDTO result = litbService.getItems(queryService.getQueryById(queryId), false);
		LOGGER.info("Start indexing items for query: " + queryService.getQueryById(queryId));

		List<String> ids = result.getInfo().getProductsList();
		if (onlyNew) {
			ids = new ArrayList<>(itemService.getNonExistIds(ids));
		}
		
		List<SolrItemDTO> items = searchService.getItems(ids);
		
		indexService.addItems(items);
		
		if (onlyNew) {
			Iterator<ItemDTO> iterator = result.getInfo().getItems().iterator();
			while (iterator.hasNext()) {
				ItemDTO dto = iterator.next();
				if (!ids.contains(dto.getItemId())) {
					iterator.remove();
				}
			}
		}
		itemService.addNewItems(result.getInfo().getItems());
		LOGGER.info("Indexed {} items.", ids.size());

		
		return searchService.query(ids, SolrCore.EVAL);
	}

	@RequestMapping(value = "indexAll", method = RequestMethod.GET, produces = "application/json")
	public String indexAll() {
		LOGGER.info("Start indexing items for the evaluation model.");
		long start = System.currentTimeMillis();
		int num = 0;
		List<EvalQuery> queries = queryRepo.findByEffectiveTrue();
		for (EvalQuery query : queries) {
			LOGGER.info("Start indexing query: " + query);
			SearchResultDTO result = litbService.search(query.getName(), false);
			List<String> ids = result.getInfo().getItems();
			List<SolrItemDTO> items = searchService.getItems(ids);
			indexService.addItems(items);
			num += items.size();
		}
		double elapse = (System.currentTimeMillis() - start) / 1000.0;
		String message = String.format("Indexed %d items, costs %.2f seconds", num, elapse);
		LOGGER.info(message);

		return message;
	}

	@RequestMapping(value = "index", method = RequestMethod.GET, produces = "application/json")
	public String index(@RequestParam String id) {
		SolrItemDTO item = searchService.getItem(id);
		if (item != null) {
			indexService.addItem(item);
		}

		return searchService.query(id, SolrCore.EVAL);
	}

	@RequestMapping(value = "delete", method = RequestMethod.GET, produces = "application/json")
	public UpdateResponse delete(@RequestParam String id) {
		return indexService.deleteItem(id);
	}

	@RequestMapping(value = "deleteAll", method = RequestMethod.GET, produces = "application/json")
	public UpdateResponse delete() {
		return indexService.deleteAll();
	}
	
	@RequestMapping(value = "clearAnnotations", method = RequestMethod.GET, produces = "application/json")
	public String clear(@RequestParam String queryID) {
		indexService.clearRelevance(queryID);
		itemService.clearAnnotation(Integer.valueOf(queryID));
		return "done";
	}
	
	@RequestMapping(value = "syncDB", method = RequestMethod.GET, produces = "application/json")
	public String syncDB() {
		Set<EvalItem> items = itemService.syncDBAndSolr();
		return "Sync " + items.size() + " new items";
	}
	
	@RequestMapping(value = "syncSolr", method = RequestMethod.GET, produces = "application/json")
	public String syncSolr(@RequestParam String field) {
		List<String> ids = indexService.getItemIdsWithEmptyField(field);
		indexService.setItemFieldValues(field, ids);
		return "Sync " + ids.size() + " items";
	}
}
