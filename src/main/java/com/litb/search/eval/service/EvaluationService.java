package com.litb.search.eval.service;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.solr.client.solrj.SolrServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.litb.search.eval.dto.SolrItemDTO;
import com.litb.search.eval.repository.QueryRepository;

@Service
public class EvaluationService {

	private static final Logger LOGGER = LoggerFactory.getLogger(EvaluationService.class); 
	
	@Autowired
	@Qualifier("SolrEvalServer")
	private SolrServer solrServer;
	
	@Autowired
	private SolrSearchService searchService;
	
	@Autowired
	private LitbSearchService litbService;
	
	@Value("${search.size}")
	private int maxSize;
	
	@Autowired
	private QueryRepository queryRepo;
	
	public double map() {
		double map = 0;
		Map<Integer, String> queries = queryRepo.getAllQueries();
		
		for (Entry<Integer, String> queryEntry : queries.entrySet()) {
			double ap = 0;
			int n = 0; // total items 
			int r = 0; // relevant items
			List<String> ids = litbService.search(queryEntry.getValue()).getInfo().getItems();
			List<SolrItemDTO> items = searchService.getItemRelevance(ids);
			for (int i = 0; i < Math.min(maxSize, items.size()); i++) {
				n++;
				SolrItemDTO item = items.get(i);
				String annotate = item.getQuery(String.valueOf(queryEntry.getKey()));
				if ( annotate != null && Integer.valueOf(annotate) > 0) {
					r++;
					ap += r/n;
					// TODO break, if all relevant items are included
				}
			}
			map += ap;
		}
		
		LOGGER.info("Solr Search Engine Mean Average Precision (MAP): " + map);
		return map;
	}

	public String pn(String query) {
		return null;
	}
}
