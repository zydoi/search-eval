package com.litb.search.eval.repository;

import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.litb.search.eval.entity.EvalQuery;
import com.litb.search.eval.service.util.KeywordParser;

@Repository
public class QueryLoader {

	private static final Logger LOGGER = LoggerFactory.getLogger(QueryLoader.class);
	
	@Autowired
	private QueryRepository repo;
	
	@PostConstruct
	public void loadAllQueries() {
		invalidQueries();
		loadQueries(QueryType.TOP);
		loadQueries(QueryType.BAD);
	}
	
	private void loadQueries(QueryType type) {
		Map<Integer, String> top = KeywordParser.parse(type.getFileName());
		for (Entry<Integer, String> e: top.entrySet()) {
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
}
