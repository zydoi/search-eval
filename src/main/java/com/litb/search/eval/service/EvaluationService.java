package com.litb.search.eval.service;

import java.util.Set;

import org.apache.solr.client.solrj.SolrServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class EvaluationService {

	private static final Logger LOGGER = LoggerFactory.getLogger(EvaluationService.class); 
	
	@Autowired
	@Qualifier("SolrEvalServer")
	private SolrServer solrServer;
	
	public String annotate(String annotator, String query, Set<Integer> ids) {
		StringBuilder sb = new StringBuilder("Annotator: ");
		sb.append(annotator).append(", Query: ").append(query).append(", ids: ");
		for (int id : ids) {
			sb.append(id).append(",");
		}
		LOGGER.info(sb.toString());
		

		
		return null;
	}
}
