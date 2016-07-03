package com.litb.search.eval.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.litb.search.eval.App;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(App.class)
public class QueryRepositoryTest {

	@Autowired
	private QueryRepository repo;
	
	@Test
	public void test() {
		System.out.println("### Total Top Query: " + repo.findByQueryType(QueryType.TOP).size());
	}
}
