package com.litb.search.eval.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrInputDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.litb.search.eval.repository.QueryRepository;
import com.litb.search.eval.service.util.SolrQueryUtils;

@Service
public class AnnotateService {
	
	private static final Logger ANNOTATE_LOGGER = LoggerFactory.getLogger("annotate"); 

	private static final Logger LOGGER = LoggerFactory.getLogger(AnnotateService.class); 

	@Autowired
	@Qualifier("SolrEvalServer")
	private SolrServer solrServer;
	
	@Autowired
	private QueryRepository queries;
	
	public void annotate(String annotator, String queryID, Set<Integer> ids) {
		String query = queries.getQueryByID(queryID);
		StringBuilder sb = new StringBuilder("Annotator: ");
		sb.append(annotator).append(", Query: ").append(query).append(", ids: ");
		
		Set<SolrInputDocument> docs = new HashSet<>();
		Map<String, Object> queryInc = new HashMap<>();
		queryInc.put("inc", 1);

		for (int id : ids) {
			sb.append(id).append(",");

			SolrInputDocument doc = new SolrInputDocument();
			doc.addField("id", id);
			doc.addField(SolrQueryUtils.QUERY_RELEVANCE_PRIFIX + queryID, queryInc);
			docs.add(doc);
		}
		
		try {
			solrServer.add(docs);
			solrServer.commit();
		} catch (SolrServerException | IOException e) {
			LOGGER.error("Failed to increase annotation!", e);
			return;
		}
		ANNOTATE_LOGGER.info(sb.toString());
	}
}