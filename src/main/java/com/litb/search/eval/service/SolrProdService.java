package com.litb.search.eval.service;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrRequest.METHOD;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.litb.search.eval.dto.SolrCore;
import com.litb.search.eval.dto.solr.SolrItemDTO;
import com.litb.search.eval.service.util.SolrConstants;
import com.litb.search.eval.service.util.SolrQueryUtils;

@Service
public class SolrProdService {

	private static final Logger LOGGER = LoggerFactory.getLogger(SolrProdService.class);

	@Autowired
	@Qualifier("SolrTemplate")
	private RestTemplate restTemplate;

	@Autowired
	@Qualifier("SolrProdServer")
	private SolrServer solrServer;

	@Autowired
	private Environment environment;

	public String query(String id, SolrCore core) {
		String url = "solr." + core.name().toLowerCase() + ".url";
		String solrAPI = environment.getProperty(url) + "select";
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(solrAPI).queryParam("q", "id:" + id);
		URI uri = builder.build().encode().toUri();
		return restTemplate.getForObject(uri, String.class);
	}

	public String query(Collection<String> ids, SolrCore core) {
		String url = "solr." + core.name().toLowerCase() + ".url";
		String solrAPI = environment.getProperty(url) + "select";
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(solrAPI).queryParam("q",
				SolrQueryUtils.concatIDs(ids));
		URI uri = builder.build().encode().toUri();
		return restTemplate.getForObject(uri, String.class);
	}

	public List<SolrItemDTO> getItems(Collection<String> ids) {
		List<SolrItemDTO> items = new ArrayList<>();
		
		Iterator<String> iterator = ids.iterator();
		StringBuilder sb = new StringBuilder("id:(");
		int count = 0;
		while (iterator.hasNext()) {
			String id = iterator.next();
			sb.append(id).append(" ");
			count++;
			if (count == SolrConstants.MAX_ID_SIZE) {
				sb.append(")");
				items.addAll(getItems(sb.toString()));
				
				sb.setLength(0);
				sb.append("id:(");
				count = 0;
			}
		}
		
		if (sb.length() > 4) {
			sb.append(")");
			items.addAll(getItems(sb.toString()));
		}
		
		LOGGER.info("Finished retrieving {} items.", items.size());
		
		return items;
	}
	
	private List<SolrItemDTO> getItems(String queryStr) {
		LOGGER.info("Start to searching products: {}", queryStr);

		SolrQuery query = new SolrQuery();
		query.setQuery(queryStr);
		query.setRows(Integer.valueOf(environment.getProperty("search.size", "100")));
		try {
			QueryResponse rsp = solrServer.query(query, METHOD.POST);
			return rsp.getBeans(SolrItemDTO.class);
		} catch (SolrServerException e) {
			LOGGER.error("Failed to search items.", e);
		}
		return new ArrayList<>();
	}

	public SolrItemDTO getItem(String id) {
		SolrQuery query = new SolrQuery();
		query.setQuery("id:" + id);
		try {
			QueryResponse rsp = solrServer.query(query);
			List<SolrItemDTO> items = rsp.getBeans(SolrItemDTO.class);
			if (!items.isEmpty()) {
				return items.get(0);
			} 
		} catch (SolrServerException e) {
			LOGGER.error("Failed to search by id.", e);
		}
		return null;
	}
}