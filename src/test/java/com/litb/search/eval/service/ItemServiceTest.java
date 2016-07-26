package com.litb.search.eval.service;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.litb.search.eval.App;
import com.litb.search.eval.service.util.TestDataGenerator;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(App.class)
public class ItemServiceTest {

	@Autowired
	private ItemService service;

	@Autowired
	private TestDataGenerator generator;
	
	@Before
	public void setup() {
		generator.generateItemsWithAnnotations();
	}
	
	@Test
	@Ignore
	public void test() {
		// service.syncDBAndSolr();
		// service.addItemDetails();
		service.clearSolrAndDB();
	}

	@Test
	public void testGetNotAnnotatedIds() {
		List<String> ids = new ArrayList<>();
		ids.add("222");
		ids.add("111");
		assertEquals("111", service.getNotAnnotatedItemIds(1, ids).get(0));
	}
	
	@After
	public void teardown() {
		generator.clearAll();
	}
}
