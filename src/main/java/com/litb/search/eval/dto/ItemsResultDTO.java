package com.litb.search.eval.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ItemsResultDTO {

	private String result;
	
	@JsonProperty("info")
	private LitbInfoDTO info;

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public LitbInfoDTO getInfo() {
		return info;
	}

	public void setInfo(LitbInfoDTO info) {
		this.info = info;
	}
}