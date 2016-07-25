package com.litb.search.eval.service.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.litb.search.eval.repository.QueryType;

public class KeywordParser {

	private static final Logger LOGGER = LoggerFactory.getLogger(KeywordParser.class);

	public static Map<Integer, String> parseAll() {
		Map<Integer, String> queries = new TreeMap<>();
		queries.putAll(parse(QueryType.TOP.getFileName()));
		queries.putAll(parse(QueryType.BAD.getFileName()));
		// queries.putAll(parse(QueryType.MISSPELL.fileName));
		// queries.putAll(parse(QueryType.SYNM.fileName));

		return queries;
	}

	public static Map<Integer, String> parse(String fileName) {
		LOGGER.info("Loading queries in: " + fileName);
		Map<Integer, String> queries = new TreeMap<>();
		String line = null;
		try (BufferedReader br = new BufferedReader(
				new InputStreamReader(KeywordParser.class.getClassLoader().getResourceAsStream(fileName)))) {
			while ((line = br.readLine()) != null) {
				StringTokenizer st = new StringTokenizer(line, "\t");
				queries.put(Integer.valueOf(st.nextToken()), st.nextToken());
			}
		} catch (NumberFormatException | IOException e) {
			LOGGER.error("Failed to parse file " + fileName + ".", e);

		}
		return queries;
	}
}
