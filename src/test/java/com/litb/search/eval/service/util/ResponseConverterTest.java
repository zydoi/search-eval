package com.litb.search.eval.service.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.litb.search.eval.App;
import com.litb.search.eval.dto.SolrCore;
import com.litb.search.eval.service.SolrSearchService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(App.class)
public class ResponseConverterTest {

	
	@Autowired
	private SolrSearchService searchService;
	
	@Test
	public void test() {
		String response = searchService.query("GB18030TEST", SolrCore.prod);
		System.out.println(response);
		
//		String response = "<doc>123</doc>";
		System.out.println(ResponseConverter.convertToIndexRequest(response));
	}
}
