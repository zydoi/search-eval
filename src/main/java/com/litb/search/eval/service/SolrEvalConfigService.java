package com.litb.search.eval.service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SolrEvalConfigService {

	private static final Logger LOGGER = LoggerFactory.getLogger(SolrEvalConfigService.class);

	private static final String SOLR_PROPERTIES = "solr.properties";
	
	@Value("${zeus.home}")
	private String zeusHome;

	private Properties props;

	public Map<String, String> loadSolrProperties() {
		return null;
	}

	public boolean updateSolrProps(Map<String, String> toUpdate) {
		if (props == null) {
			props = loadSolrProps();
		}

		try (FileOutputStream out = new FileOutputStream(zeusHome + "/etc/" + SOLR_PROPERTIES)) {
			for (Entry<String, String> prop : toUpdate.entrySet()) {
				props.setProperty(prop.getKey(), prop.getValue());
			}
			props.store(out, null);
			return true;
		} catch (FileNotFoundException e) {
			LOGGER.error("Solr properties file cannot be found. ", e);
		} catch (IOException e) {
			LOGGER.error("Unable to load solr properties ", e);
		}
		return false;
	}

	public Properties loadSolrProps() {
		props = new Properties();
		String fileName = zeusHome + "/etc/" + SOLR_PROPERTIES;
		try (FileInputStream in = new FileInputStream(fileName)) {
			props.load(in);
			LOGGER.info("Load solr configurationg file: {}", fileName);
		} catch (FileNotFoundException e) {
			LOGGER.info("Solr properties file does not exist. ");
		} catch (IOException e) {
			LOGGER.error("Unable to load solr properties ", e);
		}
		return props;
	}
}
