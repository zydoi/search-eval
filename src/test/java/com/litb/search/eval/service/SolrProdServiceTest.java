package com.litb.search.eval.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.litb.search.eval.App;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(App.class)
public class SolrProdServiceTest {

	@Autowired
	private SolrProdService service;
	
	@Test
	public void test() {
//		service.getItem("123");
	}
	

}
