package com.litb.search.eval.service;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.solr.client.solrj.SolrServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.litb.search.eval.dto.EvalResultDTO;
import com.litb.search.eval.dto.QueryEvalResultDTO;
import com.litb.search.eval.dto.SolrItemDTO;
import com.litb.search.eval.repository.QueryRepository;
import com.litb.search.eval.repository.QueryType;
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
	
	@Value("${quary.top.eval}")
	private String topQueryIDs;

	@Value("${quary.bad.eval}")
	private String badQueryIDs;
	
	@Autowired
	private QueryRepository queryRepo;
	
	private List<Integer> nums = Arrays.asList(10, 20, 48);
	
	public EvalResultDTO generateEvaluationResult(QueryType type) {
		EvalResultDTO result = new EvalResultDTO();
		double map = 0;
		List<String> qids = getEvalQueryIDs(type);
		for (String qid : qids) {
			QueryEvalResultDTO queryResult = new QueryEvalResultDTO(qid);
			double ap = 0; // average precision
			int n = 0; // total items 
			double r = 0; // relevant items
			String query = queryRepo.getQueryByID(qid);
			queryResult.setQueryName(query);
			
			List<String> ids = litbService.search(query, true).getInfo().getItems();
			List<SolrItemDTO> items = evalService.getItemWithRelevance(ids, maxSize);
			for (int i = 0; i < Math.min(maxSize, items.size()); i++) {
				n++;
				if(nums.contains(n)) {
					double p = r/n;
					queryResult.addPrecision(n, p);
				}
				
				SolrItemDTO item = items.get(i);
				int relevance = item.getQuery(SolrQueryUtils.QUERY_RELEVANCE_PRIFIX + qid);
				if (relevance > 0) {
					r++;
					ap += r/n;
					// TODO break, if all relevant items are included
				}
			}
			ap = ap / r;
			queryResult.setAp(ap);
			result.addQueryResult(queryResult);
			LOGGER.info("Average Precision for the query(" + qid + ") '" + query + "' is: " + ap);
			map += ap;
		}
		map = map / qids.size();
		result.setMap(map);
		
		LOGGER.info("Search Engine Mean Average Precision (MAP): " + map);
		return result;
	}
	
	public Map<String, Double> map(QueryType type) {
		Map<String, Double> scores = new HashMap<>();
		double map = 0;
		List<String> qids = getEvalQueryIDs(type);
		for (String qid : qids) {
			double ap = 0;
			int n = 0; // total items 
			double r = 0; // relevant items
			String query = queryRepo.getQueryByID(qid);
			List<String> ids = litbService.search(query, true).getInfo().getItems();
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
			DecimalFormat formatter = new DecimalFormat("#0.000");
			LOGGER.info("Average Precision for the query(" + qid + ") '" + query + "' is: " +  formatter.format(ap));
			map += ap;
		}
		map = map / qids.size();
		scores.put("map", map);
		
		LOGGER.info("Search Engine Mean Average Precision (MAP): " + map);
		return scores;
	}

	public double pn(String queryID, int n) {
		String query = queryRepo.getQueryByID(queryID);
		List<String> ids = litbService.search(query, true).getInfo().getItems();
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
	
	public Map<Integer, Double> getPrecisions(String queryID) {
		Map<Integer, Double> results = new HashMap<>();
		Set<Integer> nums = new HashSet<>();
		nums.add(10);
		nums.add(20);
		nums.add(48);
		String query = queryRepo.getQueryByID(queryID);
		List<String> ids = litbService.search(query, true).getInfo().getItems();
		List<SolrItemDTO> items = evalService.getItemWithRelevance(ids, 48);
		double r = 0;
		int i = 0;
		for (SolrItemDTO item : items) {
			i++;
			if (item.getQuery(SolrQueryUtils.QUERY_RELEVANCE_PRIFIX + queryID) > 0) {
				r++;
			}
			if (nums.contains(i)) {
				results.put(i, r/i);
			}
		}
		
		return results;
	}
	
	public List<String> getEvalQueryIDs(QueryType type) {
		String[] qids;
		switch (type) {
		case TOP:
			qids = topQueryIDs.split(",");
			break;
		case BAD:
			qids = badQueryIDs.split(",");
			break;
		default:
			qids = queryIDs.split(",");
			break;
		} 
		return Arrays.asList(qids);
	}
}
