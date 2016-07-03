package com.litb.search.eval.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.litb.search.eval.dto.solr.SolrItemDTO;
import com.litb.search.eval.service.util.SolrQueryUtils;

@Service
public class SolrEvalService {

	private static final Logger LOGGER = Logger.getLogger(SolrEvalService.class);

	@Autowired
	@Qualifier("SolrEvalServer")
	private SolrServer solrServer;

	@Value("${search.size}")
	private int querySize;

	public List<SolrItemDTO> getAllItems() {
		
		SolrQuery query = new SolrQuery("*:*");
		query.setFields("id", "name", "query_*");
		
		try {
			return solrServer.query(query).getBeans(SolrItemDTO.class);
		} catch (SolrServerException e) {
			LOGGER.error("Failed to get items by the query id.", e);
		}
		return new ArrayList<>();
	}
	
	public List<SolrItemDTO> getItemsByQuery(int queryId) {
		
		SolrQuery query = new SolrQuery(SolrQueryUtils.QUERY_RELEVANCE_PRIFIX + queryId + ":*");
		query.setFields("id");
		query.setFields("name");
		try {
			return solrServer.query(query).getBeans(SolrItemDTO.class);
		} catch (SolrServerException e) {
			LOGGER.error("Failed to get items by the query id.", e);
		}
		return new ArrayList<>();
	}
	
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
			LOGGER.error("Failed to delete item: " + id, e);
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

	public List<SolrItemDTO> getItemWithRelevance(Collection<String> ids, int size) {
		SolrQuery query = new SolrQuery();
		query.setQuery(SolrQueryUtils.concatIDs(ids));
		query.setFields("id", "query_*");
		query.setRows((size > 0) ? size : querySize);
		try {
			QueryResponse rsp = solrServer.query(query);
			return rsp.getBeans(SolrItemDTO.class);
		} catch (SolrServerException e) {
			LOGGER.error("Failed to get item for evaluation.", e);
		}
		return null;
	}

	public void clearRelevance(String queryId) {
		SolrQuery query = new SolrQuery();
		query.setQuery(SolrQueryUtils.QUERY_RELEVANCE_PRIFIX + queryId + ":*");
		query.setFields("id");
		List<SolrItemDTO> items = null;
		try {
			items = solrServer.query(query).getBeans(SolrItemDTO.class);
		} catch (SolrServerException e) {
			LOGGER.error("Failed to get items by the query id.", e);
		}

		if (items != null) {
			Map<String, Object> queryReset = new HashMap<>();
			queryReset.put("set", 0);
			Set<SolrInputDocument> docs = new HashSet<>();

			for (SolrItemDTO item : items) {
				SolrInputDocument doc = new SolrInputDocument();

				doc.addField("id", item.getId());
				doc.addField(SolrQueryUtils.QUERY_RELEVANCE_PRIFIX + queryId, queryReset);
				docs.add(doc);
			}
			try {
				solrServer.add(docs);
				solrServer.commit();
			} catch (SolrServerException | IOException e) {
				LOGGER.error("Failed to clear query relevance.", e);
			}
		}
	}

	public List<String> getIDs(Collection<?> ids) {
		SolrQuery query = new SolrQuery();
		query.setQuery(SolrQueryUtils.concatIDs(ids));
		query.setRows(ids.size());
		query.setFields("id");
		try {
			QueryResponse rsp = solrServer.query(query);
			SolrDocumentList docs = rsp.getResults();
			List<String> results = new ArrayList<>();
			for (SolrDocument doc : docs) {
				results.add((String) doc.getFieldValue("id"));
			}
			return results;

		} catch (SolrServerException e) {
			LOGGER.error("Failed to get ids.", e);
		}
		return new ArrayList<>();
	}

	public List<String> getNonExsitIDs(Collection<?> ids) {
		List<String> results = new ArrayList<>();
		List<String> existIDs = getIDs(ids);
		if (existIDs.size() == ids.size()) {
			return results;
		}
		for (Object id : ids) {
			if (!existIDs.contains(String.valueOf(id))) {
				results.add(String.valueOf(id));
			}
		}
		return results;
	}
}
