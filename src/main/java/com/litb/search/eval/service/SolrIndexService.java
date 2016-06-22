package com.litb.search.eval.service;

import java.io.IOException;
import java.util.Collection;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.UpdateResponse;
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

	public UpdateResponse addItem(SolrItemDTO item) {
		try {
			solrServer.addBean(item);
			solrServer.commit();
		} catch (IOException | SolrServerException e) {
			LOGGER.error("Failed to add the new document.", e);
		}
		return null;
	}

	public UpdateResponse addItems(Collection<SolrItemDTO> items) {
		try {
			solrServer.addBeans(items);
			return solrServer.commit();
		} catch (SolrServerException | IOException e) {
			LOGGER.error("Failed to add new item.", e);
		}
		return null;
	}
	
	public UpdateResponse deleteItem(String id) {
		try {
			solrServer.deleteById(id);
			return solrServer.commit();

		} catch (SolrServerException | IOException e) {
			LOGGER.error("Failed to delete item: " + id , e);
		}
		return null;
	}
	
	public UpdateResponse deleteAll() {
		try {
			solrServer.deleteByQuery("*:*");
			return solrServer.commit();

		} catch (SolrServerException | IOException e) {
			LOGGER.error("Failed to delete all items", e);
		}
		return null;
	}
	
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
