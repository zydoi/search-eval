package com.litb.search.eval.service.util;

import static org.junit.Assert.assertEquals;

import java.util.Map;

import org.junit.Test;

public class KeywordParserTest {

	@Test
	public void test() {
		Map<Integer, String> results = KeywordParser.parse("top_queries.txt");
		assertEquals("dresses", results.get(1));
	}
}
