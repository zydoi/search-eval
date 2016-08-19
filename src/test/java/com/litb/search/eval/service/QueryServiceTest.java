package com.litb.search.eval.service;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.litb.search.eval.App;
import com.litb.search.eval.dto.QueryDTO;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(App.class)
public class QueryServiceTest {

	@Autowired
	private QueryService service;
	
	@Test
	public void test() {
		QueryDTO dto = new QueryDTO();
		dto.setId(9999);
		dto.setName("test");
		service.createQuery(dto);
		assertEquals("test", service.findById(9999).getName());
		dto.setName("updated");
		service.updateQuery(dto);
		assertEquals("updated", service.findById(9999).getName());
		service.deleteById(9999);
	}
	
}
