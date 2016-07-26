package com.litb.search.eval.repository;

import static org.junit.Assert.assertEquals;

import java.util.HashSet;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.litb.search.eval.App;
import com.litb.search.eval.entity.EvalItem;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(App.class)
public class ItemRepositoryTest {
	
	@Autowired
	private ItemRepository repo;
	
	@Before
	public void setup() {
		EvalItem item = new EvalItem("123", "test1");
		EvalItem item2 = new EvalItem("222", "test2");
		
		repo.save(item);
		repo.save(item2);
		
	}
	
	@Test
	public void test() {
		Set<String> ids = new HashSet<>();
		ids.add("123");
		ids.add("111");
		
		assertEquals(2, repo.findByItemURLIsNull().size());
		assertEquals("123", repo.findExistsIds(ids).iterator().next());
	}
	
	@After
	public void tearDown() {
		repo.deleteAll();
	}
	
}
