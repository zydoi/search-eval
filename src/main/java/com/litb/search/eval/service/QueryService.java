package com.litb.search.eval.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.litb.search.eval.dto.QueryDTO;
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
	@Transactional
	public void loadAllQueries() {
		invalidQueries();
		loadQueries(QueryType.TOP);
		loadQueries(QueryType.BAD);
//		loadQueries(QueryType.SYNM);
//		loadQueries(QueryType.MISSPELL);
//		List<EvalQuery> queries = findQueriesByType(QueryType.ALL);
//		for (EvalQuery query : queries) {
//			queryMap.put(query.getId(), query.getName());
//		}
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
			query.addQueryType(type);
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
			Iterable<EvalQuery> iter = repo.findAll();
			List<EvalQuery> queries = new ArrayList<>();
			for (EvalQuery query : iter) {
				queries.add(query);
			}
			return queries;
		}
		return repo.findByQueryTypes(type);
	}
	
	public EvalQuery updateQuery(QueryDTO dto) {
		EvalQuery query = repo.findOne(dto.getId());
		query.setEffective(dto.isEffective());
		query.setName(dto.getName());
		query.setQueryTypes(dto.getTypes());
		return query;
	}
	
	public EvalQuery findById(int id) {
		return repo.findOne(id);
	}
}
