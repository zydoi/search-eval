package com.litb.search.eval.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.litb.search.eval.App;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(App.class)
public class TestService {

	@Autowired
	@Qualifier("SolrEvalServer")
	private SolrServer solrServer;

	@Test
	public void test() throws SolrServerException, IOException {

		SolrInputDocument doc = new SolrInputDocument();
		SolrInputDocument doc2 = new SolrInputDocument();
		Map<String, Object> queryInc = new HashMap<>();
		queryInc.put("inc", 1);
		doc.addField("id", "6H500F0");
		doc2.addField("id", "SP2514N");
		doc.addField("price", queryInc);
		doc2.addField("price", queryInc);
		
		Set<SolrInputDocument> docs = new HashSet<>();
		docs.add(doc2);
		docs.add(doc);
		solrServer.add(docs);
		solrServer.commit();
	}
}
