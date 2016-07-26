package com.litb.search.eval.service;

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
import com.litb.search.eval.entity.EvalItemAnnotation;
import com.litb.search.eval.repository.AnnotationRepository;
import com.litb.search.eval.service.util.TestDataGenerator;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(App.class)
public class AnnotateServiceTest {

	@Autowired 
	private AnnotationRepository repo;

	@Autowired
	private AnnotateService service;

	@Autowired
	private TestDataGenerator generator;
	
	@Before
	public void setup() {
		generator.generateItemsWithAnnotations();
	}
	
	@Test
	public void test() {
		Set<String> ids = new HashSet<>();
		ids.add("111");
		ids.add("222");
		service.unannotateItems(1, ids);
		for (EvalItemAnnotation anno: repo.findByQueryIdAndItemIdIn(1, ids)) {
			assertEquals(0, anno.getAnnotatedTimes());
		}
	}
	
	@After
	public void teardown() {
		generator.clearAll();
	}
	
}
