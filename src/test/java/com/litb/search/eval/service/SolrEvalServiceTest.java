package com.litb.search.eval.service;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.litb.search.eval.App;
import com.litb.search.eval.dto.solr.SolrItemDTO;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(App.class)
public class SolrEvalServiceTest {

	@Autowired
	private SolrEvalService service;
	
	@Test
	public void test() {
		List<String> ids = new ArrayList<>();
		ids.add("4489703");
		ids.add("123");
		
		assertEquals("4489703", service.getIDs(ids).get(0));
		assertEquals("123", service.getNonExsitIDs(ids).get(0));
		List<SolrItemDTO> items = service.getItemWithRelevance(ids, 10);
		System.out.println("Item name:" + items.get(0).getName());
		System.out.println("Query relevance:" + items.get(0).getQuery("query_1"));
	}
	
	@Test
	public void testGetAllItems() {
		List<SolrItemDTO> items = service.getAllItems();
		System.out.println("### Total " + items.size() + " items.");
	}
	
}
