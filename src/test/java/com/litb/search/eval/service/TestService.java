package com.litb.search.eval.service;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.litb.search.eval.App;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(App.class)
public class TestService {

	@Autowired
	@Qualifier("SolrEvalServer")
	private SolrServer solrServer;

	@Autowired
	@Qualifier("SolrProdServer")
	private SolrServer solrOnlineServer;

	@Test
	@Ignore
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

	@Test
	@Ignore
	public void testIncDynamicField() throws SolrServerException, IOException {

		SolrInputDocument doc = new SolrInputDocument();
		Map<String, Object> queryInc = new HashMap<>();
		queryInc.put("inc", 1);
		doc.addField("id", "3823505");
		doc.addField("query_1", queryInc);

		Set<SolrInputDocument> docs = new HashSet<>();
		docs.add(doc);
		solrServer.add(docs);
		solrServer.commit();
	}

	@Test
	public void getTopProducts() throws SolrServerException {
		HttpSolrServer server = new HttpSolrServer("http://23.253.5.4:8080/solr/categorynew/");
		List<String> cids = parseCSV("categories.csv");
		try (CSVWriter csvWriter = new CSVWriter(new FileWriter("test.csv"), ',', CSVWriter.NO_QUOTE_CHARACTER)) {
			for (String cid : cids) {
				System.out.println("Get top items for category: " + cid );
				SolrQuery query = new SolrQuery();
				query.setQuery("cate_" + cid + ":*");
				query.setRows(10000);
				query.setFields("pid");
				query.addSort("cate_" + cid, SolrQuery.ORDER.asc);
				QueryResponse response = server.query(query);
				SolrDocumentList docs = response.getResults();
				List<String> results = new ArrayList<>();
				results.add("Category: " + cid);
				for (SolrDocument doc : docs) {
					results.add((String) doc.getFieldValue("pid"));
				}
				String[] ids = results.toArray(new String[results.size()]);
				csvWriter.writeNext(ids);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private List<String> parseCSV(String csvName) {
		List<String> categories = new ArrayList<>();
		try (CSVReader reader = new CSVReader(new FileReader(csvName))) {
			String[] nextLine;
			while ((nextLine = reader.readNext()) != null) {
				categories.add(nextLine[0]);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return categories;
	}
}
