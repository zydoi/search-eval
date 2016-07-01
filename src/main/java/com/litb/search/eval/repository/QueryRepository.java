package com.litb.search.eval.repository;

import java.util.Map;
import java.util.TreeMap;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

import com.litb.search.eval.service.util.KeywordParser;

@Service
public class QueryRepository {

	private Map<Integer, String> queries;

	private Map<QueryType, Map<Integer, String>> queryMap;

	@PostConstruct
	public void loadAllQueries() {
		queries = new TreeMap<>();
		queryMap = new TreeMap<>();
		Map<Integer, String> top = KeywordParser.parse(QueryType.TOP.getFileName());
		Map<Integer, String> bad = KeywordParser.parse(QueryType.BAD.getFileName());
		queryMap.put(QueryType.TOP, top);
		queryMap.put(QueryType.BAD, bad);
		queries.putAll(top);
		queries.putAll(bad);
		queryMap.put(QueryType.ALL, queries);

	}

	public Map<Integer, String> getAllQueries() {
		return queries;
	}

	public String getQueryByID(String id) {
		return queries.get(Integer.valueOf(id));
	}

	public Map<Integer, String> getQueriesByType(QueryType type) {
		return queryMap.get(type);
	}
}
