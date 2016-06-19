package com.litb.search.eval.service;

import java.io.IOException;
import java.util.Collection;

import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.litb.search.eval.dto.SolrItemDTO;

@Service
public class SolrIndexService {
	
	private static final Logger LOGGER = Logger.getLogger(SolrIndexService.class);

	@Autowired
	@Qualifier("SolrEvalServer")
	private SolrServer solrServer;

	public void addItem(SolrItemDTO item) {
		try {
			solrServer.addBean(item);
			solrServer.commit();
		} catch (IOException | SolrServerException e) {
			LOGGER.error("Failed to add the new document.", e);
		}
	}

	public void addItems(Collection<SolrItemDTO> items) {
		try {
			solrServer.addBeans(items);
			solrServer.commit();
		} catch (SolrServerException | IOException e) {
			LOGGER.error("Failed to add new documents.", e);
		}
	}
}
