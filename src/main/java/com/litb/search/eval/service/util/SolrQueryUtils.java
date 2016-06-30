package com.litb.search.eval.service.util;

import java.util.Collection;

public final class SolrQueryUtils {
	
	public static final String QUERY_RELEVANCE_PRIFIX = "query_";
	
	public static String concatIDs(Collection<?> ids) {
		StringBuilder q = new StringBuilder("id:(");
		for (Object id : ids) {
			q.append(id).append(" ");
		}
		q.append(")");
		return q.toString();
	}
}
