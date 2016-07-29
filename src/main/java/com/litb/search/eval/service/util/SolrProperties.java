package com.litb.search.eval.service.util;

import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Properties;

public class SolrProperties extends Properties {
	
	private static final long serialVersionUID = -1014789713087685776L;
	
	private final HashSet<Object> keys = new LinkedHashSet<>();
    
    public SolrProperties() {
    }
    
    public Iterable<Object> orderedKeys() {
        return Collections.list(keys());
    }
    
    @Override
    public Enumeration<Object> keys() {
        return Collections.<Object>enumeration(keys);
    }
    
    @Override
    public Object put(Object key, Object value) {
        keys.add(key);
        return super.put(key, value);
    }
}
