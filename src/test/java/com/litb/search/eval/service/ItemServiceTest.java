package com.litb.search.eval.service;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.litb.search.eval.App;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(App.class)
public class ItemServiceTest {

	@Autowired
	private ItemService service;

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
		ids.add("4489703");
		ids.add("123");
		assertEquals("123", service.getNotAnnotatedIds(1, ids).get(0));
	}
}
