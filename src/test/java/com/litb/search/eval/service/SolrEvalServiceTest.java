package com.litb.search.eval.service;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.litb.search.eval.App;
import com.litb.search.eval.dto.SolrItemDTO;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(App.class)
public class SolrEvalServiceTest {

	@Autowired
	private SolrEvalService service;
	
	@Test
	public void test() {
		List<String> ids = new ArrayList<>();
		ids.add("4489703");
		List<SolrItemDTO> items = service.getItemWithRelevance(ids, 10);
		System.out.println("Item name:" + items.get(0).getName());
		System.out.println("Query relevance:" + items.get(0).getQuery("query_1"));
	}
}
