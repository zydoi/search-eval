package com.litb.search.eval.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SearchResultDTO {

	private String result;

	private InfoDTO info;

	public SearchResultDTO() {
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public InfoDTO getInfo() {
		return info;
	}

	public void setInfo(InfoDTO info) {
		this.info = info;
	}
}
