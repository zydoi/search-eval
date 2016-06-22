package com.litb.search.eval.service;

import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

import com.litb.search.eval.service.util.KeywordParser;

@Service
public class KeywordService {

	private Map<Integer, String> queries;
	
	@PostConstruct
	private void loadAllQueries() {
		queries = KeywordParser.parseAll();
	}
	
	public Map<Integer, String> getAllQueries() {
		return queries;
	}
	
	public String getQueryByID(String id) {
		return queries.get(Integer.valueOf(id));
	}
}
