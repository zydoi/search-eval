package com.litb.search.eval;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;


@SpringBootApplication
public class App {

	@Bean(name="LitbTemplate")
	public RestTemplate getLitbTemplate() {
		return new RestTemplate();
	}
	
	@Bean(name="SolrTemplate")
	public RestTemplate getSolrTemplate() {
		return new RestTemplate();
	}
	
	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
	}
}
