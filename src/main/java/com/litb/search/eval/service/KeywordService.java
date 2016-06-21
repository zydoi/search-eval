package com.litb.search.eval.service;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.litb.search.eval.service.util.KeywordParser;

@Service
public class KeywordService {

	
	public Map<Integer, String> loadAllQueries() {
		
		return KeywordParser.parseAll();
	}
}
