package com.litb.search.eval.service;

import java.net.URI;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.litb.search.eval.dto.SolrCore;
import com.litb.search.eval.dto.SolrItemDTO;

@Service
public class SolrSearchService {

	private static final Logger LOGGER = Logger.getLogger(SolrSearchService.class);

	@Autowired
	@Qualifier("SolrTemplate")
	private RestTemplate restTemplate;

	@Autowired
	@Qualifier("SolrProdServer")
	private SolrServer solrServer;

	@Autowired
	private Environment environment;

	public String query(String id, SolrCore core) {
		String url = "solr." + core + ".url";
		String solrAPI = environment.getProperty(url) + "select";
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(solrAPI).queryParam("q", "id:" + id);
		URI uri = builder.build().encode().toUri();
		return restTemplate.getForObject(uri, String.class);
	}

	public List<SolrItemDTO> getItems(Set<String> ids) {
		SolrQuery query = new SolrQuery();
		StringBuilder q = new StringBuilder("id:(");
		for (String id : ids) {
			q.append(id).append(" ");
		}
		q.append(")");
		query.setQuery(q.toString());
		try {
			QueryResponse rsp = solrServer.query(query);
			return rsp.getBeans(SolrItemDTO.class);
		} catch (SolrServerException e) {
			LOGGER.error("Faild to search by id.", e);
		}
		return null;
	}

	public SolrItemDTO getItem(String id) {
		SolrQuery query = new SolrQuery();
		query.setQuery("id:" + id);
		try {
			QueryResponse rsp = solrServer.query(query);
			return rsp.getBeans(SolrItemDTO.class).get(0);
		} catch (SolrServerException e) {
			LOGGER.error("Faild to search by id.", e);
		}
		return null;
	}
}