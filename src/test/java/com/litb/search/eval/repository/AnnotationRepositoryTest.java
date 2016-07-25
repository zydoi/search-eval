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
import com.litb.search.eval.entity.EvalItemAnnotation;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(App.class)
public class AnnotationRepositoryTest {

	@Autowired
	private AnnotationRepository repo;
	
	@Autowired
	private ItemRepository itemRepo;
	
	@Autowired
	private QueryRepository queryRepo;
	
	@Before
	public void setup() {
		EvalItem item = new EvalItem("123", "test");
		EvalItem item2 = new EvalItem("222", "test");
		itemRepo.save(item);
		itemRepo.save(item2);
		EvalItemAnnotation a = new EvalItemAnnotation(queryRepo.findOne(1), item);
		EvalItemAnnotation a2 = new EvalItemAnnotation(queryRepo.findOne(1), item2);
		a.setAnnotatedTimes(1);
		repo.save(a);
		repo.save(a2);
	}
	
	@Test
	public void testFindByQueryIdAndItemId() {
		assertEquals(1,repo.findByQueryIdAndItemId(1, "123").getAnnotatedTimes());
		assertEquals(2, repo.findByQueryId(1).size());
		assertEquals(2, repo.findAnnotatedItemIds(1).size());
		assertEquals(1, repo.findRelevantItemIds(1).size());
		
		Set<String> ids = new HashSet<>();
		ids.add("123");
		assertEquals(1, repo.findByQueryIdAndItemIdIn(1, ids).size());

	}
	
	@After
	public void teardown() {
		itemRepo.deleteAll();
	}
	
	
}
