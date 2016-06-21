package com.litb.search.eval.service.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;

public class KeywordParser {

	private static final Logger LOGGER = Logger.getLogger(KeywordParser.class);
	
	public enum QueryType {
		TOP("top_queries.txt"), BAD("bad_queries.txt"), MISSPELL("mispell_queriex.txt"), SYNM("synm_queries.txt");
		
		String fileName;
		
		QueryType(String fileName) {
			this.fileName = fileName;
		}
	}

	public static Map<Integer, String> parseAll() {
		Map<Integer, String> queries = new HashMap<>();
		queries.putAll(parse(QueryType.TOP.fileName));
		queries.putAll(parse(QueryType.BAD.fileName));
//		queries.putAll(parse(QueryType.MISSPELL.fileName));
//		queries.putAll(parse(QueryType.SYNM.fileName));
		
		return queries;
	}

	public static Map<Integer, String> parse(String fileName) {
		File file = new File(KeywordParser.class.getClassLoader().getResource(fileName).getFile());
		Map<Integer, String> queries = new HashMap<>();
		if (file.exists()) {
			try (Scanner scanner = new Scanner(file)) {
				while (scanner.hasNextLine()) {
					StringTokenizer st = new StringTokenizer(scanner.nextLine(), "\t");
					queries.put(Integer.valueOf(st.nextToken()),st.nextToken());
				}
			} catch (FileNotFoundException e) {
				LOGGER.error("File " + fileName + " not exist.", e);
			}
		}
		return queries;
	}
}
