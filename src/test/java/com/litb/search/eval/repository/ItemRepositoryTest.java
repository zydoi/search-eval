package com.litb.search.eval.repository;

import static org.junit.Assert.assertEquals;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.litb.search.eval.App;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(App.class)
public class ItemRepositoryTest {
	
	@Autowired
	private ItemRepository repo;
	
	@Test
	public void test() {
		Set<String> ids = new HashSet<>();
		ids.add("4351385");
		ids.add("123");
		assertEquals(1, repo.findIdById(ids).size());
	}
}
