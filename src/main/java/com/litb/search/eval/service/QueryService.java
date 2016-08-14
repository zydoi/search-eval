package com.litb.search.eval.service;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.litb.search.eval.entity.EvalQuery;
import com.litb.search.eval.repository.QueryRepository;
import com.litb.search.eval.repository.QueryType;
import com.litb.search.eval.service.util.KeywordParser;

@Service
public class QueryService {

	private static final Logger LOGGER = LoggerFactory.getLogger(QueryService.class);
	
	@Autowired
	private QueryRepository repo;
	
	private Map<Integer, String> queryMap = new TreeMap<>();
	
	@PostConstruct
	public void loadAllQueries() {
		invalidQueries();
		loadQueries(QueryType.TOP);
		loadQueries(QueryType.BAD);
//		loadQueries(QueryType.SYNM);
//		loadQueries(QueryType.MISSPELL);
	}
	
	private void loadQueries(QueryType type) {
		Map<Integer, String> queries = KeywordParser.parse(type.getFileName());
		queryMap.putAll(queries);
		for (Entry<Integer, String> e: queries.entrySet()) {
			EvalQuery query = repo.findOne(e.getKey());
			
			if (query == null) {
				query = new EvalQuery(e.getKey(), e.getValue(), type);
				LOGGER.info("Add new query: {}", e.getValue());
			}
			query.setEffective(true);
			query.setQueryType(type);
			repo.save(query);
		} 
	}
	
	private void invalidQueries() {
		Iterable<EvalQuery> queries = repo.findAll();
		for (EvalQuery evalQuery : queries) {
			evalQuery.setEffective(false);
		}
		repo.save(queries);
	}
	
	public String getQueryById(int id) {
		return queryMap.get(id);
	}
	
	public List<EvalQuery> findEffectiveQueries() {
		return repo.findByEffectiveTrue();
	}
	
	public EvalQuery addQuery(EvalQuery query) {
		return repo.save(query);
	}
	
	public List<EvalQuery> findQueriesByType(QueryType type) {
		if (type.equals(QueryType.ALL)) {
		}
		return repo.findByQueryType(type);
	}
}
