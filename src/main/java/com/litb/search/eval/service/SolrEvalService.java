package com.litb.search.eval.service;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.litb.search.eval.dto.SolrItemDTO;
import com.litb.search.eval.service.util.SolrQueryUtils;

@Service
public class SolrEvalService {
	
	private static final Logger LOGGER = Logger.getLogger(SolrEvalService.class);

	@Autowired
	@Qualifier("SolrEvalServer")
	private SolrServer solrServer;
	
	@Value("${search.size}")
	private int querySize;

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
	
	public List<SolrItemDTO> getItemRelevance(Collection<String> ids) {
		SolrQuery query = new SolrQuery();
		query.setQuery(SolrQueryUtils.concatIDs(ids));
		query.setFields("id", "query_*");
		query.setRows(querySize);
		try {
			QueryResponse rsp = solrServer.query(query);
			return rsp.getBeans(SolrItemDTO.class);
		} catch (SolrServerException e) {
			LOGGER.error("Failed to get item for evaluation.", e);
		}
		return null;
	}
}
