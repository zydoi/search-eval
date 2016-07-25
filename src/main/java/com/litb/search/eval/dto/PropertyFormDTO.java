package com.litb.search.eval.dto;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class PropertyFormDTO {

	private Map<String, String> properties = new HashMap<>();
	
	public PropertyFormDTO() {}

	public PropertyFormDTO(Properties props) {
		for (String name : props.stringPropertyNames()) {
			properties.put(name, props.getProperty(name));
		}
	}
	
	public Map<String, String> getProperties() {
		return properties;
	}

	public void setProperties(Map<String, String> properties) {
		this.properties = properties;
	}
	
	
}
