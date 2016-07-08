package com.litb.search.eval.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.litb.search.eval.App;
import com.litb.search.eval.entity.EvalItemAnnotation;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(App.class)
public class AnnotationRepositoryTest {

	@Autowired
	private AnnotationRepository repo;
	
	@Test
	public void testFindByQueryIdAndItemId() {
		EvalItemAnnotation annotation = repo.findByQueryIdAndItemId(1, "4489703");
		assertEquals(2,annotation.getAnnotatedTimes());
	}
	
	@Test
	public void testFindByQueryId() {
		assertEquals(1, repo.findByQueryId(1).get(0).getQuery().getId());
//		assertEquals(84, repo.findAnnotatedItemIds(1).size());
		assertTrue(repo.findRelevantItemIds(1).contains("4489703"));
		
	}
}
