package com.litb.search.eval.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.litb.search.eval.dto.SolrItemDTO;
import com.litb.search.eval.repository.QueryRepository;
import com.litb.search.eval.service.util.SolrQueryUtils;

@Service
public class EvaluationService {

	private static final Logger LOGGER = LoggerFactory.getLogger(EvaluationService.class); 
	
	@Autowired
	@Qualifier("SolrEvalServer")
	private SolrServer solrServer;
	
	@Autowired
	private SolrEvalService evalService;
	
	@Autowired
	private LitbSearchService litbService;
	
	@Value("${search.size}")
	private int maxSize;
	
	@Value("${quary.eval}")
	private String queryIDs;
	
	@Autowired
	private QueryRepository queryRepo;
	
	public Map<String, Double> map() {
		Map<String, Double> scores = new HashMap<>();
		double map = 0;
		String[] qids = queryIDs.split(",");
		for (String qid : qids) {
			double ap = 0;
			int n = 0; // total items 
			double r = 0; // relevant items
			String query = queryRepo.getQueryByID(qid);
			List<String> ids = litbService.search(query).getInfo().getItems();
			List<SolrItemDTO> items = evalService.getItemWithRelevance(ids, maxSize);
			for (int i = 0; i < Math.min(maxSize, items.size()); i++) {
				n++;
				SolrItemDTO item = items.get(i);
				int annotate = item.getQuery(SolrQueryUtils.QUERY_RELEVANCE_PRIFIX + qid);
				if (annotate > 0) {
					r++;
					ap += r/n;
					// TODO break, if all relevant items are included
				}
			}
			scores.put(query, ap);
			LOGGER.info("Average Precision for the query(" + qid + ") '" + query + "' is: " + ap);
			map += ap;
		}
		map = map / qids.length;
		scores.put("map", map);
		
		LOGGER.info("Search Engine Mean Average Precision (MAP): " + map);
		return scores;
	}

	public double pn(String queryID, int n) {
		String query = queryRepo.getQueryByID(queryID);
		List<String> ids = litbService.search(query).getInfo().getItems();
		List<SolrItemDTO> items = evalService.getItemWithRelevance(ids, n);
		double r = 0;
		for (SolrItemDTO item : items) {
			if (item.getQuery(SolrQueryUtils.QUERY_RELEVANCE_PRIFIX + queryID) > 0) {
				r++;
			} 
		}
		
		int total = Math.min(n, items.size());
		return r / total;
	}
}
