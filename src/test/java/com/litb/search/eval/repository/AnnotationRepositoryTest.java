package com.litb.search.eval.repository;

import static org.junit.Assert.assertEquals;

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
	public void test() {
		EvalItemAnnotation annotation = repo.findByQueryIdAndItemId(21, "4775008");
		assertEquals(1,annotation.getAnnotatedTimes());
	}
}
